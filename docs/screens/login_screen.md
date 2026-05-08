# LoginScreen

## 結構總覽

```
LoginScreen (有 ViewModel)
  └── LoginScreenContent (無狀態，只接收參數)
        ├── ModalNavigationDrawer (側邊選單)
        │     └── Scaffold
        │           ├── LoadingDialog (Loading 時顯示)
        │           └── Column (主體)
        │                 ├── Top Bar（Menu icon / Logo / 語言）
        │                 ├── Center Logo（GIF 動畫）
        │                 └── Bottom Section（Login / Telegram 按鈕）
        ├── LoginBottomSheet（if showLoginSheet）
        └── VerifyMobileDialog（if state is NeedsVerification）
```

---

## Composable 說明

### `LoginScreen`
**有 ViewModel 的入口層**，負責：
- 訂閱 `viewModel.loginState`（透過 `collectAsState()`）
- 偵測到 `Success` 狀態時執行 `onNavigateToHome()`
- 把所有操作（login、resetState、verifyOtp）包裝後傳給 `LoginScreenContent`

```kotlin
val state by viewModel.loginState.collectAsState()

LaunchedEffect(state) {
    if (state is LoginState.Success) onNavigateToHome()
}
```

> 這層只做「狀態訂閱 + 導航」，不含任何 UI。

---

### `LoginScreenContent`
**無狀態的純 UI 層**，接收外部參數驅動畫面，自身只管理兩個 local state：

| Local State | 用途 |
|------------|------|
| `showLoginSheet` | 控制 `LoginBottomSheet` 顯示/隱藏 |
| `drawerState` | 控制側邊選單開關 |

**關鍵邏輯：**

```kotlin
// 成功或需要驗證 → 自動關閉底部彈窗
LaunchedEffect(state) {
    if (state is LoginState.Success || state is LoginState.NeedsVerification) {
        showLoginSheet = false
    }
}
```

---

## 畫面三個區塊

### Top Bar
```
[Menu Icon]  [ic_xcash Logo]  [EN 語言]
```
- Menu icon 點擊 → `drawerState.open()`

### Center Logo
- 用 **Coil + GifDecoder** 顯示 `xcash_logo_type2.gif`
- `remember(context)` 確保 `ImageLoader` 只建立一次

### Bottom Section
- **Login 按鈕** → `showLoginSheet = true`（打開底部彈窗）
- **Telegram 按鈕** → 預留（目前 `onClick = {}`）
- Loading 中兩顆按鈕都 `enabled = false`

---

## 條件顯示的元件

### `LoginBottomSheet`

```kotlin
if (showLoginSheet) {
    LoginBottomSheet(
        onDismissRequest = { showLoginSheet = false; onResetState() },
        onLoginSubmit = { phone, pwd -> onLoginClick(phone, pwd) },
        errorMessage = (state as? LoginState.Error)?.message
    )
}
```

| 觸發條件 | 行為 |
|---------|------|
| 按 Login 按鈕 | 開啟 |
| API 成功 / 需驗證 | `LaunchedEffect` 自動關閉 |
| 用戶手動關閉（backdrop / back） | `onDismissRequest` 關閉並呼叫 `onResetState()` |
| API 回傳錯誤 | **不關閉**，傳入 `errorMessage` 顯示於輸入框下方 |

### `VerifyMobileDialog`

```kotlin
if (state is LoginState.NeedsVerification) {
    VerifyMobileDialog(...)
}
```

- 當 API 回傳 code `2001` 時，ViewModel 設定 `NeedsVerification` 狀態
- Dialog 顯示，讓用戶輸入 OTP

---

## 資料流向

```
用戶操作
  → LoginScreenContent (local event)
  → ViewModel.login() / verifyOtp() / resetState()
  → LoginState (StateFlow)
  → LoginScreen.collectAsState()
  → 傳入 LoginScreenContent(state = ...) → UI 更新
```

---

## LoginState 狀態機

```
Idle
  ↓ login()
Loading
  ↓ API 成功          ↓ code == 2001          ↓ 其他錯誤
Success          NeedsVerification            Error
  ↓                    ↓ verifyOtp()
導航到 Home         Loading → Success / Error
```

---

## 檔案結構

```
ui/login/
├── DrawerMenuContent.kt       # 側邊選單內容
├── LoginScreen.kt             # 頁面入口 + LoginScreenContent
├── LoginViewModel.kt          # 狀態管理
└── dialog/                    # Dialog 類別集中管理
    ├── AccountStatusDialog.kt # 帳號狀態 Dialog
    ├── LoginBottomSheet.kt    # 登入底部彈窗
    └── VerifyMobileDialog.kt  # OTP 驗證 Dialog
```

`DrawerMenuContent.kt` 和 `LoginScreen.kt` 需明確 import `dialog` 子套件：

```kotlin
// DrawerMenuContent.kt
import com.example.newproject.ui.login.dialog.AccountStatusDialog

// LoginScreen.kt
import com.example.newproject.ui.login.dialog.LoginBottomSheet
import com.example.newproject.ui.login.dialog.VerifyMobileDialog
```

---

## 注意事項

- `LoginScreenContent` 是無狀態設計，可直接用於 `@Preview`
- `LoginBottomSheet` 使用 `Dialog` 包裝，確保父層 recompose 時不會重置內部 `isVisible` 狀態
- `AppNavigation` 收到 logout 事件時會檢查當前路由，若已在 `login` 頁面則不重複導航（避免 code 1005 帳密錯誤時重建畫面）
