# ScanPayScreen

## 結構總覽

```
ScanPayScreen（無 ViewModel，本地管理狀態）
  ├── selectedMode（rememberSaveable：QrMode.SCAN_QR / QrMode.MY_QR）
  ├── QrModeTabSelector（Tab 切換列）
  └── when (selectedMode)
        ├── SCAN_QR → ScanQrContent
        │     └── 正方形 Box（neonCyan 邊框 + 提示文字，相機占位）
        └── MY_QR  → MyQrContent（verticalScroll）
              ├── QR Code Section（bg_qrcode 背景圖）
              │     ├── username（neonCyan，頂部）
              │     ├── 180×180 neonBlue Box（QR 占位）
              │     └── name（底部）
              ├── Balance Section
              │     ├── ic_car 圖示（neonPurple 光暈）
              │     ├── Current Balance 文字 + 金額切換（明文 / ••••）
              │     ├── Visibility Toggle icon
              │     ├── X-Points 文字
              │     └── ic_monkey 圖示（balanceGold 光暈）
              ├── Action Buttons（兩欄 Row）
              │     ├── MyQrActionButton（ic_gift，Generate Ang Pao / Gift QR）
              │     └── MyQrActionButton（ic_money，Split a Bill & Request）
              └── Daily Quests Card（neonCyan 光暈邊框卡片）
                    ├── 標題文字 + 進度（0/3 Scans）
                    ├── LinearProgressIndicator（neonCyan）
                    └── ic_girl 圖示
```

---

## Composable 說明

### `ScanPayScreen`

目前**直接管理本地狀態**，無 ViewModel 注入。  
`ScanPayViewModel` 已建立但為空殼，尚未接入 UiState 或 eventFlow。

```kotlin
var selectedMode by rememberSaveable { mutableStateOf(QrMode.MY_QR) }
```

- 預設顯示 `MY_QR` 分頁
- 整個畫面結構為 `Column`，`horizontalAlignment = CenterHorizontally`
- 無 `onNavigate` / `onBack` 參數（畫面由 `CustomBottomNavigation` 的 overlay 開關）

---

### `ScanQrContent`（private）

相機掃描 QR 的占位畫面，目前為純 UI，無實際 CameraX 整合：

- 外層 `Box`：`fillMaxWidth + aspectRatio(1f)` 正方形
- `neonCyan` 邊框（2.dp）、`RoundedCornerShape(16.dp)`
- 中央顯示 `scan_pay_hint` 提示文字

> 尚未整合相機模組，待 CameraX 或其他 QR scanner library 接入。

---

### `MyQrContent`（private）

MY QR 分頁的完整 UI，以 `Column + verticalScroll` 佈局，由上至下分為四個區塊：

#### QR Code Section
- 以 `bg_qrcode`（mipmap）作為背景圖（`ContentScale.FillWidth`）
- 疊加 username（頂部）與 name（底部）文字
- 中央 `180×180 neonBlue Box` 為 QR Code 占位符

#### Balance Section
- 三欄橫排：`ic_car` 圖示、中央資訊欄（`weight(1f)`）、`ic_monkey` 圖示
- `isBalanceVisible`（`remember { mutableStateOf(false) }`）控制餘額明文 / `••••` 切換
- Visibility toggle 使用 `indication = null` 的 `clickable`

#### Action Buttons
- 兩個等寬 `MyQrActionButton`（`weight(1f)`）橫排
- `Generate Ang Pao / Gift QR`（ic_gift）
- `Split a Bill & Request`（ic_money）

#### Daily Quests Card
- `neonCyan` 光暈邊框卡片（`neonGlow + border + background`）
- `LinearProgressIndicator(progress = 0f)`（尚未接入動態進度）
- 右側 `ic_girl` 圖示裝飾

---

### `MyQrActionButton`（private）

統一的動作按鈕元件：

| 參數 | 類型 | 說明 |
|------|------|------|
| `iconRes` | `Int` | mipmap / drawable 資源 ID |
| `label` | `String` | 按鈕文字（支援換行，`lineHeight = 15.sp`） |
| `modifier` | `Modifier` | 外部尺寸控制（通常傳入 `weight(1f)`） |
| `onClick` | `() -> Unit` | 點擊回呼，預設空 |

- 外框：`neonPurple` 邊框 + 光暈
- 圖示圓圈：`neonCyan` 邊框 + 光暈
- 圖示使用 `tint = Color.Unspecified` 保留原色

---

### `QrModeTabSelector`（ui/components/）

共用元件，定義於 `ui/components/QrModeTabSelector.kt`：

```kotlin
enum class QrMode { SCAN_QR, MY_QR }

fun QrModeTabSelector(
    selectedMode: QrMode,
    onModeChange: (QrMode) -> Unit,
    modifier: Modifier = Modifier
)
```

- 外框：`welcomeBackground` 底色 + `neonPurple/neonCyan` 漸層邊框
- 選中 Tab：`neonCyan` 膠囊背景（文字黑色）
- 未選 Tab：透明背景（文字 `normalText`）
- 高度固定 `44.dp`，內部留白 `5.dp`

---

## ScanPayUiState

```kotlin
data class ScanPayUiState(
    val isLoading: Boolean = false
)
```

目前為**占位結構**，尚未連接任何資料欄位。

---

## 資料流向（目前）

```
ScanPayScreen
  ├── selectedMode（local rememberSaveable）→ 控制 Tab 切換
  └── isBalanceVisible（local remember）→ 控制餘額顯示 / 隱藏
```

所有資料目前為硬編碼，尚未接入 API：

| 資料項目 | 目前狀態 |
|----------|----------|
| `qrcode_url` | 硬編碼字串（宣告但未使用） |
| 餘額金額 | 硬編碼 `"PHP 1000"` |
| X-Points | 硬編碼字串資源 `5,000 X-Points` |
| Quest 進度 | 固定 `progress = 0f` / `"0/3 Scans"` |
| 使用者名稱 / 姓名 | 硬編碼字串資源 |

---

## 檔案結構

```
ui/scanpay/
├── ScanPayScreen.kt    # 頁面入口 + ScanQrContent + MyQrContent + MyQrActionButton
└── ScanPayViewModel.kt # ScanPayUiState（占位）/ ScanPayViewModel（空殼）

ui/components/
└── QrModeTabSelector.kt # QrMode enum + QrModeTabSelector + QrTab
```

---

## 注意事項

- `ScanPayScreen` 目前**沒有接入 ViewModel**，所有狀態為 local，待 API 接入後需重構為標準 MVVM 分層（`ScanPayScreenContent` + `ScanPayViewModel`）
- `ScanQrContent` 是純 UI 占位，相機功能尚未整合
- Daily Quests Card 的 `scan_pay_my_qr_daily_quest_progress` 字串在標題行和進度條下方各出現一次（待確認是否為重複）
- 餘額切換使用 `indication = null` 的 `clickable`，避免深色背景出現矩形 ripple
- `bg_qrcode` 背景圖以 `ContentScale.FillWidth` 填滿寬度，QR Code 區域以 `180×180 neonBlue Box` 占位
- `QrModeTabSelector` 為共用元件，可在其他需要雙模式切換的頁面重用
