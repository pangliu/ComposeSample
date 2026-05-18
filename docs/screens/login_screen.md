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
        │     └── 生物辨識按鈕（if showBiometricButton）
        ├── VerifyMobileDialog（if state is NeedsVerification）
        └── BiometricEnrollDialog（if state is PromptBiometricEnroll）
```

---

## Composable 說明

### `LoginScreen`
**有 ViewModel 的入口層**，負責：
- 訂閱 `viewModel.loginState` 與 `viewModel.hasSavedCredentials`（透過 `collectAsState()`）
- 偵測到 `Success` 狀態時執行 `onNavigateToHome()`
- 把所有操作包裝後傳給 `LoginScreenContent`，包含：
  - `onBiometricLoginSuccess` → `viewModel.loginWithStoredCredentials()`
  - `onBiometricEnrollSuccess` → `viewModel.enrollBiometric()`
  - `onBiometricEnrollSkip` → `viewModel.skipBiometricEnroll()`
  - `onShowBiometricPromptForLogin` / `onShowBiometricPromptForEnroll` → 呼叫 `BiometricHelper.showPrompt()`

```kotlin
val state by viewModel.loginState.collectAsState()
val hasSavedCredentials by viewModel.hasSavedCredentials.collectAsState()
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
// 成功、需要驗證、或要顯示生物辨識綁定 → 自動關閉底部彈窗
LaunchedEffect(state) {
    if (state is LoginState.Success ||
        state is LoginState.NeedsVerification ||
        state is LoginState.PromptBiometricEnroll
    ) {
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
        errorMessage = (state as? LoginState.Error)?.message,
        showBiometricButton = showBiometricButton,
        onBiometricLogin = { onShowBiometricPromptForLogin() }
    )
}
```

| 觸發條件 | 行為 |
|---------|------|
| 按 Login 按鈕 | 開啟 |
| API 成功 / 需驗證 / 顯示生物辨識綁定 | `LaunchedEffect` 自動關閉 |
| 用戶手動關閉（backdrop / back） | `onDismissRequest` 關閉並呼叫 `onResetState()` |
| API 回傳錯誤 | **不關閉**，傳入 `errorMessage` 顯示於輸入框下方 |
| 有儲存憑證且裝置支援生物辨識 | 顯示「Login with Biometrics」按鈕 |

### `VerifyMobileDialog`

```kotlin
if (state is LoginState.NeedsVerification) {
    VerifyMobileDialog(...)
}
```

- 當 API 回傳 code `2001` 時，ViewModel 設定 `NeedsVerification` 狀態
- Dialog 顯示，讓用戶輸入 OTP

### `BiometricEnrollDialog`

```kotlin
if (state is LoginState.PromptBiometricEnroll) {
    BiometricEnrollDialog(
        onEnroll = { onShowBiometricPromptForEnroll() },
        onSkip = { onBiometricEnrollSkip() }
    )
}
```

- 首次帳密登入成功後，若裝置支援生物辨識且尚未儲存憑證，ViewModel 設定 `PromptBiometricEnroll` 狀態
- 用戶點 **Enable** → 系統 `BiometricPrompt` 驗證 → 成功後呼叫 `viewModel.enrollBiometric()` 儲存加密憑證
- 用戶點 **Maybe Later** → 呼叫 `viewModel.skipBiometricEnroll()` 直接導向 Home
- OTP 驗證流程（`NeedsVerification` → `verifyOtp()`）成功後同樣觸發此流程

---

## 資料流向

```
用戶操作
  → LoginScreenContent (local event)
  → ViewModel.login() / verifyOtp() / resetState()
    / enrollBiometric() / skipBiometricEnroll() / loginWithStoredCredentials()
  → LoginState (StateFlow) + hasSavedCredentials (StateFlow)
  → LoginScreen.collectAsState()
  → 傳入 LoginScreenContent(state = ...) → UI 更新
```

---

## LoginState 狀態機

```
Idle
  ↓ login()
Loading
  ↓ API 成功                      ↓ code == 2001          ↓ 其他錯誤
  ↓                           NeedsVerification            Error
  ↓                                ↓ verifyOtp()
  ↓                           Loading → 成功 / Error
  ↓ (已有憑證 or 裝置不支援)   ↓ (無憑證 && 裝置支援)
Success                     PromptBiometricEnroll
  ↓                           ↓ [Enable] + 系統驗證成功    ↓ [Skip]
導航到 Home              enrollBiometric()          skipBiometricEnroll()
                              ↓                            ↓
                           儲存加密憑證 → Success      Success → 導航到 Home
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

`LoginScreen.kt` 需明確 import `dialog` 子套件與工具類：

```kotlin
import com.example.newproject.ui.login.dialog.BiometricEnrollDialog
import com.example.newproject.ui.login.dialog.LoginBottomSheet
import com.example.newproject.ui.login.dialog.VerifyMobileDialog
import com.example.newproject.utils.BiometricHelper
import androidx.fragment.app.FragmentActivity
```

---

## 注意事項

- `LoginScreenContent` 是無狀態設計，可直接用於 `@Preview`
- `LoginBottomSheet` 使用 `Dialog` 包裝，確保父層 recompose 時不會重置內部 `isVisible` 狀態
- `AppNavigation` 收到 logout 事件時會檢查當前路由，若已在 `login` 頁面則不重複導航（避免 code 1005 帳密錯誤時重建畫面）
- `BiometricHelper.showPrompt()` 需要 `FragmentActivity`，透過 `LocalContext.current as FragmentActivity` 取得，此呼叫集中在 `LoginScreen`（有 ViewModel 的層），不傳入 ViewModel
- `CredentialManager` 使用 `EncryptedSharedPreferences`（`secure_credential_prefs`），與 `TokenManager` 的 `app_prefs` 檔案分開
- 生物辨識按鈕只在 `hasSavedCredentials == true && BiometricHelper.isAvailable(context) == true` 時顯示，確保舊裝置或未綁定用戶不會看到無法使用的按鈕

### 生物辨識相關設定（必要，缺一不可）

**1. AndroidManifest.xml 權限**

`BiometricPrompt` 及 `FingerprintManager` 需要以下兩個 normal permission（安裝時自動授予，不需執行期請求）：

```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```

未加此權限 `canAuthenticate()` 會回傳不可用，導致綁定 Dialog 永遠不觸發。

**2. MainActivity 必須繼承 AppCompatActivity**

`BiometricPrompt` 建構子接受 `FragmentActivity`，而 `FragmentActivity` 是 `AppCompatActivity` 的父類。若 `MainActivity` 繼承 `ComponentActivity`（`FragmentActivity` 的兄弟類，非父子關係），執行 `LocalContext.current as FragmentActivity` 時會拋出：

```
java.lang.ClassCastException: MainActivity cannot be cast to androidx.fragment.app.FragmentActivity
```

修正方式：

```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() { ... }
```

並在 `app/build.gradle.kts` 加入：

```kotlin
implementation(libs.androidx.appcompat)
```

**3. 主題必須使用 AppCompat 系列**

`AppCompatActivity` 要求 App Theme 繼承自 AppCompat 系列主題，否則啟動時拋出：

```
java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
```

修正 `res/values/themes.xml`：

```xml
<!-- 錯誤：framework 原生主題 -->
<style name="Theme.NewProject" parent="android:Theme.Material.Light.NoActionBar">

<!-- 正確：AppCompat 主題 -->
<style name="Theme.NewProject" parent="Theme.AppCompat.Light.NoActionBar">
```

**4. OPPO / ColorOS 裝置注意事項**

OPPO 的臉部辨識為廠商私有實作，**不整合 Android 標準 BiometricManager**，因此：
- `canAuthenticate(BIOMETRIC_WEAK)` 回傳 `BIOMETRIC_ERROR_NONE_ENROLLED`（-11）或其他非 `BIOMETRIC_SUCCESS` 值
- 無法透過標準 `BiometricPrompt` 使用 OPPO 臉部辨識

`BiometricHelper.isAvailable()` 採用雙重偵測策略：

```kotlin
// 第一道：標準 BiometricManager（適用大多數裝置）
val result = BiometricManager.from(context).canAuthenticate(BIOMETRIC_WEAK)
if (result == BIOMETRIC_SUCCESS) return true

// 第二道：FingerprintManager 降級偵測（適用 OPPO/ColorOS 指紋）
@Suppress("DEPRECATION")
val fm = context.getSystemService(Context.FINGERPRINT_SERVICE) as? FingerprintManager
return fm?.isHardwareDetected == true && fm.hasEnrolledFingerprints() == true
```

> 注意：`BIOMETRIC_STRONG or BIOMETRIC_WEAK` 組合在 Android 10（API 29）以下回傳 `BIOMETRIC_ERROR_UNSUPPORTED`（-2），必須只傳 `BIOMETRIC_WEAK` 單一值。
