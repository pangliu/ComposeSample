# ScanPay Screen — 變更說明

本文件說明 `ui/scanpay/` 目錄下各畫面與元件的設計與實作，涵蓋本次開發期間所做的所有修改。

---

## 目錄結構

```
ui/scanpay/
├── ScanPayScreen.kt            # 入口畫面（My QR / Scan QR 切換）
├── MyQrContent.kt              # QR code 顯示、掃描、餘額、行動按鈕
├── ScanPayViewModel.kt         # 共用 ViewModel
├── InputAmountScreen.kt        # 輸入金額畫面
├── ConfirmPaymentScreen.kt     # 確認付款畫面
├── SelectSplitPartnerDialog.kt # 選擇分帳夥伴 Dialog
├── SplitBillDialog.kt          # 分帳金額設定 Dialog
├── SplitPartnersRow.kt         # 已確認分帳夥伴列表列
└── TransactionSuccessfulScreen.kt
```

---

## ScanPayScreen.kt

### 功能
掃描付款的入口畫面，包含「我的 QR Code」與「掃描 QR Code」兩個 Tab。

### 主要元件

| 元件 | 說明 |
|------|------|
| `ScanPayScreen` | Hilt entry-point composable，接收 `ScanPayViewModel` 與 `onNavigate` callback |
| `ScanPayContent` | 無狀態版本，包含 `QrModeTabSelector` 與 `MyQrContent` |
| `parseXcashQrCode(url)` | 解析 XCash QR URL 取出收款人資訊 |

### QR Code URL 格式

```
http://xcash.io/pay?account={account}&to={nickName}&name={name}
```

| 參數 | 說明 |
|------|------|
| `account` | 收款人帳號（用於實際付款，例如 `hank_001`） |
| `to` | 收款人暱稱（顯示用 @handle，例如 `hank`） |
| `name` | 收款人全名（顯示用，例如 `hank liu`，空格以 `+` 表示） |

### 掃描流程

1. 使用者切換至 Scan QR Tab
2. `CameraPreviewView`（定義於 `MyQrContent.kt`）啟動後鏡頭並以 ZXing 持續解析影格
3. 解析到符合 `http://xcash` 前綴的 URL 後呼叫 `onQrCodeScanned`
4. `ScanPayScreen` 呼叫 `viewModel.setRecipientInfo()` 儲存收款人，並導向 `InputAmountScreen`

---

## MyQrContent.kt

### 功能
顯示使用者自己的 QR Code，以及掃描他人 QR Code 的相機視圖。

### QRose 函式庫整合

**替換原因**：原本的 ZXing 只能產生純黑白 Bitmap，無法自訂顏色或形狀。改用 QRose 可在 Compose 中直接產生具有圓形像素、品牌色彩與中央 Logo 的風格化 QR Code。

**依賴版本**（`libs.versions.toml`）：
```toml
[versions]
qrose = "1.0.1"

[libraries]
qrose = { group = "io.github.alexzhirkevich", name = "qrose", version.ref = "qrose" }
```

**`app/build.gradle.kts`**：
```kotlin
// QR Code
implementation(libs.qrose)
```

### QR Code 設定

```kotlin
val qrPainter = rememberQrCodePainter(
    data = qrCodeUrl,
    shapes = QrShapes(
        ball = QrBallShape.circle(),
        darkPixel = QrPixelShape.roundCorners(),
        frame = QrFrameShape.roundCorners(.25f)
    ),
    colors = QrColors(
        dark = QrBrush.solid(neonCyan),
        light = QrBrush.solid(Color.Transparent)
    ),
    errorCorrectionLevel = QrErrorCorrectionLevel.High
)
```

**設計決策說明**：

- `light = QrBrush.solid(Color.Transparent)`：QR Code 淺色區域設為透明，讓容器背景色（`qrCodeBackground`）透出，避免黑色方塊問題
- `errorCorrectionLevel = QrErrorCorrectionLevel.High`：加入 Logo 時必須使用高容錯率，否則 Logo 覆蓋資料區域後 QR Code 無法被掃描；即使目前 Logo 已移除，仍保留此設定以備未來重新啟用
- `QrLogo` 目前被注解掉，保留設定供參考；重新啟用時需確認 `ic_qrcode_logo` 圖片為透明背景

### 掃描器實作（`CameraPreviewView` + `decodeQrFromProxy`）

- 使用 CameraX `ImageAnalysis` 持續分析影格
- ZXing `MultiFormatReader` 解析 YUV luminance source
- **反色嘗試**：若正向解碼失敗，自動嘗試 `source.invert()`，支援亮色模組 + 深色背景的 QR Code（如 neonCyan on dark）
- 使用 `lastScannedRef` 防止重複觸發同一個 QR Code
- `DisposableEffect` 確保離開畫面時正確釋放 Camera 資源

### `MyQrActionButton`

可重用的行動按鈕元件，`internal` 可見性，供 `ConfirmPaymentScreen` 共用。

```kotlin
@Composable
internal fun MyQrActionButton(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
)
```

---

## ScanPayViewModel.kt

### UiState

```kotlin
data class ScanPayUiState(
    val myUserName: String = "",
    val myNickName: String = "",
    val balance: Double = 0.0,
    val tokenBalance: Double = 0.0,
    val recipientAccount: String = "",
    val recipientNickName: String = "",
    val recipientName: String = "",
    val amount: String = "",
    val isConfirming: Boolean = false,
    val confirmErrorMessage: String = "",
    // ── Split Bill ─────────────────────────────────────
    val friendList: List<FriendResponse> = emptyList(),
    val selectedFriendList: List<FriendResponse> = emptyList(),
    val confirmedSplitPartners: List<FriendResponse> = emptyList(),
    val isFetchingFriends: Boolean = false,
    val showSelectPartnerDialog: Boolean = false,
    val showSplitBillDialog: Boolean = false
)
```

### 注入依賴

新增注入 `UserRepository` 以呼叫 `fetchFriendList()` API：

```kotlin
@HiltViewModel
class ScanPayViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository
) : ViewModel()
```

### Split Bill 相關方法

| 方法 | 說明 |
|------|------|
| `fetchFriendListAndShowDialog()` | 呼叫 API 取得好友列表，成功後設定 `showSelectPartnerDialog = true` |
| `dismissSelectPartnerDialog()` | 關閉選擇夥伴 Dialog |
| `onPartnersConfirmed(selected)` | 儲存已選取好友，並開啟分帳金額 Dialog |
| `dismissSplitBillDialog()` | 關閉分帳 Dialog |
| `confirmSplitBill()` | 將 `selectedFriendList` 搬至 `confirmedSplitPartners`，關閉 Dialog |
| `editSplitBill()` | 重新開啟分帳 Dialog（進入編輯模式） |
| `cancelSplitBill()` | 清除 `confirmedSplitPartners` |
| `clearSplitBillState()` | 清除所有分帳狀態，在進入 `ConfirmPaymentScreen` 時呼叫 |

---

## ConfirmPaymentScreen.kt

### 主要變更

1. **進入畫面時清除分帳狀態**：
   ```kotlin
   LaunchedEffect(Unit) { viewModel.clearSplitBillState() }
   ```

2. **SelectSplitPartnerDialog** 顯示條件：
   ```kotlin
   if (uiState.showSelectPartnerDialog) {
       SelectSplitPartnerDialog(
           friendList = uiState.friendList,
           totalAmount = uiState.amount.toDoubleOrNull() ?: 0.0,
           onDismiss = { viewModel.dismissSelectPartnerDialog() },
           onConfirm = { selected -> viewModel.onPartnersConfirmed(selected) }
       )
   }
   ```

3. **SplitBillDialog** 顯示條件（使用 `selectedFriendList`，僅含已選取的好友）：
   ```kotlin
   if (uiState.showSplitBillDialog) {
       SplitBillDialog(
           totalAmount = uiState.amount.toDoubleOrNull() ?: 0.0,
           friendList = uiState.selectedFriendList,
           myName = uiState.myUserName,
           onDismiss = { viewModel.dismissSplitBillDialog() },
           onConfirm = { viewModel.confirmSplitBill() }
       )
   }
   ```

4. **Split a Bill & Request 按鈕**：使用 `MyQrActionButton`，`onClick = onSplitBill`；當 `confirmErrorMessage` 不為空時隱藏，改顯示錯誤訊息。

5. **SplitPartnersRow** 顯示確認後的分帳夥伴：
   ```kotlin
   if (uiState.confirmedSplitPartners.isNotEmpty()) {
       SplitPartnersRow(
           partners = uiState.confirmedSplitPartners,
           onEdit = onEditSplit,
           onCancel = onCancelSplit
       )
   }
   ```

---

## SelectSplitPartnerDialog.kt

### 功能
讓使用者從好友列表中勾選分帳夥伴，確認後進入金額分配 Dialog。

### UI 結構

```
Dialog
└── Column
    ├── Title（neonPurple）
    ├── 已選人數提示（selectedCount > 0 → neonCyan）
    ├── LazyColumn（好友列表，每項含 Avatar、名稱、勾選圓形）
    ├── Total to Split 資訊列（InputFieldBackground）
    └── Assign Amounts 按鈕（至少選 1 人才啟用，neonPurple border）
```

### 狀態管理
- `selectedIds: Set<String>` 以好友 `id` 記錄勾選狀態
- 確認時過濾 `friendList.filter { it.id in selectedIds }` 回傳

---

## SplitBillDialog.kt

### 功能
顯示分帳夥伴與「你」的金額分配，支援 EQUALLY（平均分配）與 CUSTOM（自訂）兩種模式。

### Split Mode 切換

```
[EQUALLY]  ──[Switch]──  [CUSTOM]
```

- Switch 開啟 → CUSTOM 模式，CUSTOM 文字亮 neonCyan
- Switch 關閉 → EQUALLY 模式，EQUALLY 文字亮 neonCyan，未亮起使用 normalText

### EQUALLY 分配邏輯

```kotlin
val n = participants.size
val total = totalAmount.toInt()
val share = total / n
val remainder = total % n
// 餘數加到 "You"（index 0）
amounts[0] = "${share + remainder}"
amounts[i] = "$share"  // i > 0
```

- 所有金額均為整數（無小數點）
- 餘數放在「You」欄位
- 切換回 CUSTOM 模式時清空所有輸入框

### Remaining 計算

```kotlin
val totalAssigned = amounts.values.sumOf { it.toDoubleOrNull() ?: 0.0 }
val remaining = totalAmount - totalAssigned
```

Confirm Request 按鈕條件：
```kotlin
val isConfirmEnabled = kotlin.math.abs(remaining) < 0.01
```

### UI 結構

```
Dialog
└── Column（neonCyan border + neonGlow）
    ├── 標題（neonPurple，有陰影）
    ├── Toggle Row：EQUALLY Text + Switch + CUSTOM Text
    ├── "Manual Split" 標籤（靠右）
    ├── LazyColumn（SplitBillParticipantItem × n）
    ├── Remaining 資訊列（InputFieldBackground，flat）
    └── Confirm Request 按鈕（enabled 時 neonPurple glow）
```

### SplitBillParticipantItem

獨立 `internal` composable，含自己的 Preview：

```kotlin
@Composable
internal fun SplitBillParticipantItem(
    displayName: String,   // "You" 或 "@nickName"
    avatarLetter: String,  // 名字首字母
    amount: String,
    enabled: Boolean,      // CUSTOM 模式才可編輯
    onAmountChange: (String) -> Unit
)
```

**輸入框實作**：使用 `BasicTextField` + 自訂 `decorationBox` 達到精確 40dp 高度控制。
Material3 `OutlinedTextField` 在 BOM 2024.11.00 中不支援 `contentPadding` 參數，因此改用此方式：

```kotlin
BasicTextField(
    modifier = Modifier.height(40.dp).width(110.dp),
    decorationBox = { innerTextField ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(InputFieldBackground, RoundedCornerShape(8.dp))
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) { innerTextField() }
    }
)
```

---

## SplitPartnersRow.kt

### 功能
在 `ConfirmPaymentScreen` 中顯示已確認的分帳夥伴，最多顯示 5 個 Avatar，並提供編輯／取消按鈕。

### UI 結構

```
Row
├── Row（最多 5 個 SplitPartnerAvatar，不足以 Spacer 補位）
└── Column
    ├── Edit 按鈕（balanceGold border）
    └── Cancel 按鈕（neonPink border）
```

---

## strings.xml 新增字串

| Key | 說明 |
|-----|------|
| `split_bill_title` | SplitBillDialog 標題 |
| `split_bill_equally` | EQUALLY 模式文字 |
| `split_bill_custom` | CUSTOM 模式文字 |
| `split_bill_manual_split` | 右上角 "Manual Split" 標籤 |
| `split_bill_you` | 參與者列表中「你」的顯示名稱 |
| `split_bill_remaining` | Remaining 資訊列左側文字 |
| `split_bill_confirm_request` | Confirm Request 按鈕文字 |
| `select_partner_title` | SelectSplitPartnerDialog 標題 |
| `select_partner_selected_count` | 已選人數提示（含 count 參數） |
| `select_partner_total_to_split` | 總金額列左側文字 |
| `select_partner_assign_amounts` | Assign Amounts 按鈕文字 |

---

## 整體 Split Bill 流程

```
ConfirmPaymentScreen
│
├─ 點選「Split a Bill & Request」
│   └─ viewModel.fetchFriendListAndShowDialog()
│       └─ API: UserRepository.fetchFriendList()
│           └─ showSelectPartnerDialog = true
│
├─ SelectSplitPartnerDialog（勾選夥伴）
│   └─ 點選 Assign Amounts
│       └─ viewModel.onPartnersConfirmed(selected)
│           └─ selectedFriendList = selected
│              showSplitBillDialog = true
│
├─ SplitBillDialog（設定各人金額）
│   ├─ EQUALLY：自動平分，餘數給 You
│   ├─ CUSTOM：手動輸入各人金額
│   └─ Confirm Request（remaining ≈ 0 才可點）
│       └─ viewModel.confirmSplitBill()
│           └─ confirmedSplitPartners = selectedFriendList
│              showSplitBillDialog = false
│
└─ 顯示 SplitPartnersRow（confirmedSplitPartners）
    ├─ Edit → viewModel.editSplitBill() → 重新開啟 SplitBillDialog
    └─ Cancel → viewModel.cancelSplitBill() → 清除 confirmedSplitPartners
```
