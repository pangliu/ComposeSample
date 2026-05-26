# LoginScreen

## 結構總覽

```
LoginScreen（有 ViewModel）
  ├── LaunchedEffect → 收集 viewModel.navigationEvent（導航 / 彈窗觸發）
  ├── LaunchedEffect → 收集 viewModel.eventFlow（Toast / Dialog 顯示）
  └── LoginScreenContent（無狀態，只接收參數）
        ├── ModalNavigationDrawer（側邊選單）
        │     └── Scaffold
        │           ├── LoadingDialog（isShowing = uiState.isLoading）
        │           └── Column（主體）
        │                 ├── Top Bar（Menu icon / Logo / 語言）
        │                 ├── Center Logo（GIF 動畫）
        │                 └── Bottom Section（Login / Telegram 按鈕）
        ├── LoginBottomSheet（if showLoginSheet）
        │     └── 生物辨識按鈕（if showBiometricButton）
        ├── BiometricEnrollDialog（if showBiometricEnrollDialog）
        └── VerifyMobileDialog（if showVerifyDialog）
```

---

## Composable 說明

### `LoginScreen`
**有 ViewModel 的入口層**，負責：
- 訂閱 `viewModel.uiState` 與 `viewModel.hasSavedCredentials`（透過 `collectAsState()`）
- 管理導航驅動的 local state：`showVerifyDialog`、`verifyPhone`、`showBiometricEnrollDialog`
- `LaunchedEffect(Unit)` 收集 `viewModel.navigationEvent`，依事件更新 local state 或執行導航
- `LaunchedEffect(Unit)` 收集 `viewModel.eventFlow`，顯示 Toast / Dialog
- 把所有操作包裝後傳給 `LoginScreenContent`

```kotlin
val uiState by viewModel.uiState.collectAsState()

var showVerifyDialog by remember { mutableStateOf(false) }
var verifyPhone by remember { mutableStateOf("") }
var showBiometricEnrollDialog by remember { mutableStateOf(false) }

LaunchedEffect(Unit) {
    viewModel.navigationEvent.collect { event ->
        when (event) {
            is LoginNavigationEvent.Success -> onNavigateToHome()
            is LoginNavigationEvent.PromptBiometricEnroll -> showBiometricEnrollDialog = true
            is LoginNavigationEvent.NeedsVerification -> {
                verifyPhone = event.phone
                showVerifyDialog = true
            }
        }
    }
}

LaunchedEffect(Unit) {
    viewModel.eventFlow.collect { event ->
        when (event) {
            is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            is UiEvent.ShowDialog -> Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
        }
    }
}
```

> `BiometricHelper.showPrompt()` 需要 `FragmentActivity`，因此集中在此層透過 `LocalContext.current as FragmentActivity` 取得，不往 ViewModel 傳入 Activity 參照。

---

### `LoginScreenContent`
**無狀態的純 UI 層**，接收外部參數驅動畫面，自身管理以下 local state：

| Local State | 用途 |
|------------|------|
| `showLoginSheet` | 控制 `LoginBottomSheet` 顯示/隱藏 |
| `drawerState` | 控制側邊選單開關 |
| `showBiometricButton` | `hasSavedCredentials && BiometricHelper.isAvailable(context)` 的計算結果 |

**關鍵邏輯：**

```kotlin
// 需要驗證或顯示生物辨識綁定 → 自動關閉底部彈窗
LaunchedEffect(showVerifyDialog, showBiometricEnrollDialog) {
    if (showVerifyDialog || showBiometricEnrollDialog) {
        showLoginSheet = false
    }
}
```

**完整參數列表：**

| 參數 | 說明 |
|------|------|
| `uiState: LoginUiState` | `isLoading`、`loginError` |
| `hasSavedCredentials` | 是否有儲存的生物辨識憑證 |
| `showVerifyDialog` | 是否顯示 OTP 驗證 Dialog（由 LoginScreen 管理） |
| `verifyPhone` | OTP 驗證的目標手機號（由 LoginScreen 管理） |
| `showBiometricEnrollDialog` | 是否顯示生物辨識綁定 Dialog（由 LoginScreen 管理） |
| `onLoginClick` | 帳密登入回呼 |
| `onResetState` | 清除 `loginError`（關閉 Sheet 時呼叫） |
| `onVerifyOtp` | OTP 驗證回呼（只傳 otp，phone 由 LoginScreen 持有） |
| `onDismissVerifyDialog` | 關閉 OTP Dialog 並清除狀態 |
| `onDismissBiometricEnrollDialog` | 關閉生物辨識綁定 Dialog |
| `onBiometricEnrollSuccess` / `onBiometricEnrollSkip` | 生物辨識綁定結果回呼 |
| `onShowBiometricPromptForLogin` / `onShowBiometricPromptForEnroll` | 呼叫系統 BiometricPrompt |

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
- `uiState.isLoading` 為 `true` 時兩顆按鈕都 `enabled = false`，文字顯示 `logging_in`

---

## 條件顯示的元件

### `LoginBottomSheet`

```kotlin
if (showLoginSheet) {
    LoginBottomSheet(
        onDismissRequest = { showLoginSheet = false; onResetState() },
        onLoginSubmit = { phone, pwd -> onLoginClick(phone, pwd) },
        errorMessage = uiState.loginError,
        showBiometricButton = showBiometricButton,
        onBiometricLogin = { onShowBiometricPromptForLogin() }
    )
}
```

| 觸發條件 | 行為 |
|---------|------|
| 按 Login 按鈕 | 開啟 |
| `showVerifyDialog` 或 `showBiometricEnrollDialog` 變為 `true` | `LaunchedEffect` 自動關閉 |
| 用戶手動關閉（backdrop / back） | `onDismissRequest` 關閉並呼叫 `onResetState()` 清除 `loginError` |
| API 回傳錯誤 | **不關閉**，`uiState.loginError` 傳入 `errorMessage` 顯示於輸入框下方 |
| 有儲存憑證且裝置支援生物辨識 | 顯示「Login with Biometrics」按鈕 |

### `VerifyMobileDialog`

```kotlin
if (showVerifyDialog) {
    VerifyMobileDialog(
        initialPhone = verifyPhone,
        onDismiss = onDismissVerifyDialog,
        onSubmit = { otp -> onVerifyOtp(otp) }
    )
}
```

- 當 API 回傳 code `2001` 時，ViewModel 透過 `navigationEvent` 發出 `NeedsVerification(phone)`
- `LoginScreen` 收到後設定 `showVerifyDialog = true`、`verifyPhone = phone`
- OTP 驗證錯誤透過 `eventFlow` 顯示 Toast

### `BiometricEnrollDialog`

```kotlin
if (showBiometricEnrollDialog) {
    BiometricEnrollDialog(
        onEnroll = { onShowBiometricPromptForEnroll() },
        onSkip = { onBiometricEnrollSkip(); onDismissBiometricEnrollDialog() }
    )
}
```

- 首次帳密登入成功後，若裝置支援生物辨識且尚未儲存憑證，ViewModel 透過 `navigationEvent` 發出 `PromptBiometricEnroll`
- `LoginScreen` 收到後設定 `showBiometricEnrollDialog = true`
- 用戶點 **Enable** → 系統 `BiometricPrompt` 驗證 → 成功後呼叫 `viewModel.enrollBiometric()` 儲存加密憑證並發出 `Success`
- 用戶點 **Maybe Later** → 呼叫 `viewModel.skipBiometricEnroll()` 發出 `Success` → 導向 Home

---

## 資料流向

```
用戶操作
  → LoginScreenContent（local event）
  → ViewModel.login() / verifyOtp() / resetState()
    / enrollBiometric() / skipBiometricEnroll() / loginWithStoredCredentials()
  → _uiState（isLoading / loginError）
    _navigationEvent（Success / PromptBiometricEnroll / NeedsVerification）
    _eventFlow（ShowToast）
  → LoginScreen 收集並更新 local state 或執行導航
  → 傳入 LoginScreenContent(uiState = ...) → UI 更新
```

---

## LoginUiState + LoginNavigationEvent

### LoginUiState（純資料容器）

```kotlin
data class LoginUiState(
    val isLoading: Boolean = false,
    val loginError: String? = null   // 顯示於 LoginBottomSheet 輸入框下方
)
```

- `isLoading`：API 呼叫進行中，按鈕 disabled、文字顯示「Logging in...」
- `loginError`：帳密錯誤等登入失敗訊息，由 `LoginBottomSheet` 內嵌顯示；`onResetState()` 清除

### LoginNavigationEvent（一次性導航事件）

```kotlin
sealed class LoginNavigationEvent {
    object Success : LoginNavigationEvent()
    object PromptBiometricEnroll : LoginNavigationEvent()
    data class NeedsVerification(val phone: String) : LoginNavigationEvent()
}
```

| 事件 | 觸發條件 | 效果 |
|------|---------|------|
| `Success` | 登入/驗證/生物辨識成功 | 導航到 Home |
| `PromptBiometricEnroll` | 首次成功且裝置支援 | 顯示綁定 Dialog |
| `NeedsVerification(phone)` | API 回傳 code 2001 | 顯示 OTP Dialog |

---

## 檔案結構

```
ui/login/
├── DrawerMenuContent.kt       # 側邊選單內容
├── LoginScreen.kt             # 頁面入口 + LoginScreenContent
├── LoginViewModel.kt          # LoginUiState / LoginNavigationEvent / LoginViewModel
└── dialog/                    # Dialog 類別集中管理
    ├── AccountStatusDialog.kt # 帳號狀態 Dialog
    ├── BiometricEnrollDialog.kt # 生物辨識綁定詢問 Dialog
    ├── LoginBottomSheet.kt    # 登入底部彈窗
    └── VerifyMobileDialog.kt  # OTP 驗證 Dialog

network/manager/
├── CredentialManager.kt       # 加密儲存 biometric 登入憑證（EncryptedSharedPreferences）
└── ...

utils/
├── BiometricHelper.kt         # 封裝 BiometricPrompt（isAvailable / showPrompt）
└── ...
```

---

## 注意事項

- `LoginScreenContent` 是無狀態設計，可直接用於 `@Preview`
- 導航事件（`LoginNavigationEvent`）用 `SharedFlow` 承載，確保一次性觸發；`LoginUiState` 只存持續顯示的資料（loading 旗標、錯誤文字）
- `loginError` 是持續存在的資料（顯示於 Sheet 內），用 `uiState` 而非 `eventFlow`；OTP 驗證錯誤（一次性 Toast）才走 `eventFlow`
- `LoginBottomSheet` 使用 `Dialog` 包裝，確保父層 recompose 時不會重置內部 `isVisible` 狀態
- `AppNavigation` 收到 logout 事件時會檢查當前路由，若已在 `login` 頁面則不重複導航（避免 code 1005 帳密錯誤時重建畫面）
- `BiometricHelper.showPrompt()` 需要 `FragmentActivity`，透過 `LocalContext.current as FragmentActivity` 取得，此呼叫集中在 `LoginScreen`，不傳入 ViewModel
- `CredentialManager` 使用 `EncryptedSharedPreferences`（`secure_credential_prefs`），與 `TokenManager` 的 `app_prefs` 檔案分開

### 生物辨識相關設定（必要，缺一不可）

**1. AndroidManifest.xml 權限**

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```

**2. MainActivity 必須繼承 AppCompatActivity**

```kotlin
class MainActivity : AppCompatActivity() { ... }
```

並在 `app/build.gradle.kts` 加入：

```kotlin
implementation(libs.androidx.appcompat)
```

**3. 主題必須使用 AppCompat 系列**

```xml
<style name="Theme.NewProject" parent="Theme.AppCompat.Light.NoActionBar">
```

**4. OPPO / ColorOS 裝置注意事項**

`BiometricHelper.isAvailable()` 採用雙重偵測策略：

```kotlin
val result = BiometricManager.from(context).canAuthenticate(BIOMETRIC_WEAK)
if (result == BIOMETRIC_SUCCESS) return true

@Suppress("DEPRECATION")
val fm = context.getSystemService(Context.FINGERPRINT_SERVICE) as? FingerprintManager
return fm?.isHardwareDetected == true && fm.hasEnrolledFingerprints() == true
```

> 注意：`BIOMETRIC_STRONG or BIOMETRIC_WEAK` 組合在 Android 10（API 29）以下回傳 `BIOMETRIC_ERROR_UNSUPPORTED`（-2），必須只傳 `BIOMETRIC_WEAK` 單一值。
