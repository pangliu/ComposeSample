# Theme System（主題系統）說明

本專案支援 **THEME_NEON**（霓虹）與 **THEME_BLACK_GOLD**（黑金）兩種主題。
本文件是「顏色/資源分色規則」的依據 —— **推廣主題系統到其他頁面時，一律照本文件的步驟走。**

---

## 檔案分層架構

```
Color.kt          ← 色票（What）：純色值常數，依主題分區塊
AppColors.kt      ← 全域語意色（跨頁面共用）+ AppColors 組合根 data class
<Page>Colors.kt   ← 頁面專屬語意色（例：LoginColors.kt）
AppAssets.kt      ← 圖片/影片資源（Neon / Black Gold 各一份實例）
AppTheme.kt       ← CompositionLocal：LocalAppColors / LocalAppAssets
ThemeMode.kt      ← enum SSOT：colors / assets 兩個 when 分支
ThemeManager.kt   ← SharedPreferences("app_prefs") 持久化 ThemeMode
AppViewModel.kt   ← currentTheme: StateFlow<ThemeMode>，setTheme(mode)
AppNavigation.kt  ← 從 currentTheme 取出 colors/assets，注入最外層 AppTheme { }
```

---

## 分色規則（核心判斷準則）

新增一個色彩群組（data class）時，先問：

> **這個色彩群組除了「這一個頁面資料夾」之外，還有沒有其他頁面會用？**

| 判斷結果 | 放置位置 |
|---------|---------|
| 只有單一頁面（含其 `components/`、`dialog/`、子頁面）使用 | 該頁面自己的 `<Page>Colors.kt`（跟隨 UI 資料夾分類規則命名，例：`ui/login/` → `LoginColors.kt`） |
| 兩個以上頁面共用 | 留在 `AppColors.kt` 的全域區塊 |

**現況對照表：**

| 色彩群組 | 檔案 | 使用範圍 |
|---------|------|---------|
| `AccentColors` | `AppColors.kt` | 全域（primary / secondary / secondaryDark，多頁共用） |
| `BgColors` | `AppColors.kt` | 全域（頁面底色 / 卡片底色） |
| `TextColors` | `AppColors.kt` | 全域（body / onPrimary） |
| `EffectColors` | `AppColors.kt` | 全域（`enableGlow` 開關，Neon = true / Black Gold = false） |
| `GradientColors` | `AppColors.kt` | 全域（`goldShimmer`/`silverShimmer: Brush?` 漸層裝飾效果，Neon = null 不套用；搭配 `ui/components/GradientText.kt` 使用，見下方「漸層裝飾效果」） |
| `BalanceCardColors` | `HomeColors.kt` | 僅 `ui/home/components/BalanceCard.kt` 使用（已遷移） |
| `LoginButtonColors`、`DrawerColors`、`DrawerCardColors`、`SelectorColors`、`AccountDialogColors`、`LoginSheetColors` | `LoginColors.kt` | 僅 `ui/login/` 資料夾（Screen + components + dialog）使用 |

### `ui/home` 尚待黑金設計的項目

以下元件目前只有 Neon 設計，**尚無 Black Gold 對照色**，因此暫時維持檔案內 `private val Xxx = Color(0x...)` hardcode，不勉強塞進 `HomeColors.kt`（避免用猜的顏色污染正式設計）。待黑金設計圖到位後，比照 `BalanceCardColors` 的方式，在 `HomeColors.kt` 補上對應的 data class + `Neon<Feature>Colors` / `BlackGold<Feature>Colors`，並回填 `AppColors` 的 `NeonColors` / `BlackGoldColors`：

| 檔案 | 待遷移的 hardcode 色 |
|------|----------------------|
| `ui/home/components/XEssentialsCard.kt` | `cardGradientEnd`、`essentialEdit`、`essentialMore` |
| `ui/home/dialog/EditEssentialsDialog.kt` | （沿用 `XEssentialsCard` 同組色，見上） |
| `ui/home/dialog/DeleteAccountDialog.kt` | `DialogBg`、`InputBg`、`warringText`、`cancelText`、`DeleteButtonBg` |
| `ui/home/dialog/LogoutDialog.kt` | `DialogBg` |
| `ui/home/setting/SettingScreen.kt` | `CardBg` |
| `ui/home/notifications/components/NotificationCard.kt` | `CardBg` |
| `ui/home/transaction_detail/TransactionDetailScreen.kt` | `CardBg` |
| `ui/home/update_log/UpdateLogScreen.kt` | `CardBg` |

其餘 `ui/home` 檔案（`HomeScreen.kt`、`EssentialItems.kt`、`QuestCard.kt`、`RecentActivity.kt`、`NotificationsScreen.kt`、`NotificationSettingsDialog.kt`）目前只使用 `AppColors.kt` 的全域色（`colors.accent` / `colors.bg` / `colors.text`），已符合雙主題規則，不需額外處理。

---

## 新增頁面主題色的 SOP

以「把 `BalanceCardColors` 從 `AppColors.kt` 遷移到 `HomeColors.kt`」為例（已完成，可直接當範本套用到其他頁面）：

### Step 1 — 判斷歸屬
確認該色彩群組只被 `ui/home/` 底下的檔案使用 → 符合「抽出」條件。

### Step 2 — 建立 `<Page>Colors.kt`
命名規則比照 `LoginColors.kt`：
- data class 名稱：`<Feature>Colors`（例：`BalanceCardColors`）
- 兩份主題實例：`Neon<Feature>Colors`、`BlackGold<Feature>Colors`

```kotlin
package com.qpay.xcash.ui.theme

import androidx.compose.ui.graphics.Color

// ui/home/ 資料夾底下畫面專屬顏色（BalanceCard 等）

data class BalanceCardColors(
    val cashInBackground: Color,
    val cashInBorder: Color,
    val cashInText: Color,
)

val NeonBalanceCardColors = BalanceCardColors(
    cashInBackground = limeGreen,
    cashInBorder = Color.Transparent,
    cashInText = Color.Black,
)

val BlackGoldBalanceCardColors = BalanceCardColors(
    cashInBackground = Color.Transparent,
    cashInBorder = amberGold,
    cashInText = amberGold,
)
```

### Step 3 — 接回 `AppColors.kt`
`AppColors` data class 維持既有欄位（`balanceCard: BalanceCardColors`），只是欄位型別現在來自另一個檔案；`NeonColors` / `BlackGoldColors` 改為引用 Step 2 建立的 val：

```kotlin
val NeonColors = AppColors(
    ...
    balanceCard = NeonBalanceCardColors,
)

val BlackGoldColors = AppColors(
    ...
    balanceCard = BlackGoldBalanceCardColors,
)
```

### Step 4 — Composable 內存取
一律透過 `colors.xxx.yyy` 存取，禁止在 Composable 內 hardcode `Color(0xFF...)`：

```kotlin
val colors = LocalAppColors.current
Modifier.background(colors.balanceCard.cashInBackground)
```

### Step 5 — 若該頁面也有專屬圖片/影片資源
比照 `AppAssets.kt` 現有模式，在 `AppAssets` data class 加欄位，`NeonAssets` / `BlackGoldAssets` 各補一份值。

### Step 6 — Preview
維持雙主題寫法（見下方「Preview 標準寫法」），Neon 與 Black Gold 各一個 `@Preview`。

---

## 新增第三個主題（ThemeMode）的 SOP

若未來需要第三種主題模式，依序處理，**編譯器會在每一步強制你補齊**（除了 Step 5）：

1. **`ThemeMode.kt`**：enum 加一個 entry。`colors` / `assets` 的 `when` 分支沒有 `else`，加了新 entry 若沒補分支會直接編譯失敗。
2. **`ThemeManager.kt`**：`companion object` 加一個字串常數；`save()` 的 `when(mode)` 同樣沒有 `else`，會強制補分支；`load()` 補一個字串比對分支。
3. **`AppColors.kt` + 所有 `<Page>Colors.kt`**：每一個色彩群組 data class 各補一份新主題的 `val` 實例（人工巡覽，目前無法自動提醒，需靠本文件的「現況對照表」逐一核對）。
4. **`AppAssets.kt`**：補一份新主題的 `AppAssets` 實例。
5. **各頁面 `@Preview`**：非必要，可視情況延後補上第三份 Preview。

---

## 執行期切換機制

```
ThemeManager（SharedPreferences "app_prefs"）
  ↔ AppViewModel.currentTheme: StateFlow<ThemeMode>
       │ setTheme(mode) 同時寫入 prefs 並更新 StateFlow
       ▼
   AppNavigation：
     AppTheme(colors = currentTheme.colors, assets = currentTheme.assets) { NavHost { ... } }
       ▼ CompositionLocalProvider
   LocalAppColors / LocalAppAssets
       ▼
   各 Screen：val colors = LocalAppColors.current
```

- `ThemeMode` 是唯一的 SSOT，`colors`/`assets` 一定成對從同一個 `ThemeMode` 產生，禁止用 `AppColors` 物件參照比對（`===`）反推 assets 或持久化 key。
- 新增/切換主題只透過 `AppViewModel.setTheme(mode: ThemeMode)`，目前尚未有 UI 呼叫此方法（尚無主題切換開關）。

---

## neonGlow 規則

`neonGlow` 是純 `Modifier` extension，不含 Composable context。Black Gold 模式不需要 glow 效果，統一寫法：

```kotlin
// 正確寫法
.then(if (colors.effect.enableGlow) Modifier.neonGlow(color = colors.accent.primary, ...) else Modifier)

// 禁止直接呼叫（Black Gold 下會出現不需要的效果）
.neonGlow(color = colors.accent.primary, ...)
```

---

## 漸層裝飾效果（文字漸層）

跟 `neonGlow` 同樣屬於「只在特定主題套用的裝飾效果」，處理方式比照辦理：**效果的實作元件放 `ui/components/`（跨頁面共用），實際的漸層值放 `AppColors.kt`（是否套用、套用什麼顏色由主題決定）。**

### `GradientColors`（`AppColors.kt`）

```kotlin
data class GradientColors(
    val goldShimmer: Brush?,     // 直向金色文字漸層；Neon = null（不套用，維持單色文字）
    val silverShimmer: Brush?,   // 直向銀色文字漸層；Neon = null（不套用，維持單色文字）
)
```

- Neon：`goldShimmer = null`、`silverShimmer = null`
- Black Gold：
  - `goldShimmer = Brush.verticalGradient(listOf(oldGold, amberGold, champagneGold, amberGold, oldGold))`
  - `silverShimmer = Brush.verticalGradient(listOf(graphiteGray, steelGray, silverMist, steelGray, graphiteGray))`

若之後需要新增其他漸層效果（例如水平漸層、其他配色），比照 `goldShimmer`/`silverShimmer` 在 `GradientColors` 裡加欄位，而不是散落到個別頁面的 `<Page>Colors.kt`——因為漸層是「效果」而非某個頁面專屬的顏色值，跟 `EffectColors.enableGlow` 同一層級，理當放全域。

漸層也可以套用在 `Modifier.border(width, brush, shape)` 上（`androidx.compose.foundation.border` 本身支援 `Brush` 多載），Neon 時用 `colors.gradient.xxxShimmer ?: SolidColor(純色 fallback)` 包一層即可相容：

```kotlin
.border(
    width = 1.dp,
    brush = colors.gradient.silverShimmer ?: SolidColor(colors.balanceCard.sendBorder),
    shape = RoundedCornerShape(8.dp)
)
```

### `GradientText`（`ui/components/GradientText.kt`）

```kotlin
@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    brush: Brush? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
)
```

`brush` 不為 null 時顯示漸層，為 null 時退回 `color` 單色。呼叫端統一寫法：

```kotlin
GradientText(
    text = "...",
    color = mistGray,                       // Neon fallback（goldShimmer 為 null 時使用）
    brush = colors.gradient.goldShimmer,     // Black Gold 套用漸層，Neon 自動變回單色
    fontSize = 16.sp,
    fontWeight = FontWeight.ExtraBold,
)
```

- **不要**在頁面裡直接寫 `Text(style = TextStyle(brush = ...))`，一律透過 `GradientText`，未來要調整漸層效果（例如換成 Brush 動畫）只需改一個檔案
- 目前套用範圍：
  - `goldShimmer`：`BalanceCard.kt` 的 `balance_title`、金額、`tokenBalance`、Cash In 文字與邊框
  - `silverShimmer`：`BalanceCard.kt` 的 Send 文字與邊框

---

## Preview 標準寫法

```kotlin
@Preview(name = "Neon", showBackground = true, backgroundColor = 0xFF030F1B)
@Composable
private fun XxxPreviewNeon() {
    AppTheme(colors = NeonColors, assets = NeonAssets) { XxxScreenContent() }
}

@Preview(name = "Black Gold", showBackground = true, backgroundColor = 0xFF050505)
@Composable
private fun XxxPreviewBlackGold() {
    AppTheme(colors = BlackGoldColors, assets = BlackGoldAssets) { XxxScreenContent() }
}
```

若該畫面沒有專屬 assets，`AppTheme(colors = ...)` 可省略 `assets` 參數（沿用預設 `NeonAssets`）。

---

## 為什麼不用「每個頁面各寫一份 Compose UI」

曾評估過的替代方案：每個 mode 各寫一份完整 Composable 樹（而非共用結構、只換色）。結論是**不採用**，原因：

- UI 結構、State 處理、導航 callback 等「跟顏色無關的邏輯」也會被複製成 N 份，任何非顏色的修改都要在每份複本裡各改一次（shotgun surgery）。
- 兩份複本容易在無人察覺的情況下產生**行為分岔**，不只是顏色不同。
- 維護成本隨「頁面數 × 主題數」成長，而非現行架構的「頁面數 + 頁面數 × 主題數的資料實例」。

現行架構把「會因主題而變的東西」（顏色、圖片、要不要 glow）與「不會變的東西」（畫面結構、互動邏輯）切開，只有真正需要分歧的地方（如 `enableGlow` 開關）才用局部 `if` 條件式，而非整頁複製。

---

## 相關檔案對照

| 檔案 | 職責 |
|------|------|
| `ui/theme/Color.kt` | 色票常數 |
| `ui/theme/AppColors.kt` | 全域語意色 + `AppColors` 組合根 |
| `ui/theme/LoginColors.kt` | `ui/login/` 專屬語意色 |
| `ui/theme/HomeColors.kt` | `ui/home/` 專屬語意色 |
| `ui/theme/AppAssets.kt` | 圖片/影片資源（Neon / Black Gold） |
| `ui/components/GradientText.kt` | 跨頁面共用的漸層文字元件，搭配 `AppColors.gradient` 使用 |
| `ui/theme/AppTheme.kt` | `LocalAppColors` / `LocalAppAssets` CompositionLocal |
| `ui/theme/ThemeMode.kt` | 主題種類 enum（SSOT），`colors` / `assets` 計算屬性 |
| `network/manager/ThemeManager.kt` | SharedPreferences 持久化 `ThemeMode` |
| `ui/AppViewModel.kt` | `currentTheme: StateFlow<ThemeMode>`、`setTheme()` |
| `ui/AppNavigation.kt` | 從 `currentTheme` 注入最外層 `AppTheme { }` |
