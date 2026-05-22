# ProfileScreen

## 結構總覽

```
ProfileScreen（有 ViewModel）
  └── ProfileScreenContent（無狀態，只接收參數）
        ├── ProfileSectionHeader × 5
        ├── IdentityCard
        │     └── VerifiedBadge（if isVerified）
        ├── SocialRewardsCard
        ├── ProfileMenuCard（Account）
        │     └── ProfileMenuItem × 3
        ├── ProfileMenuCard（Security）
        │     └── ProfileMenuItem × 1
        ├── ProfileMenuCard（Support）
        │     └── ProfileMenuItem × 2
        ├── Error Text（if ProfileState.Error）
        └── LogoutButton
```

---

## Composable 說明

### `ProfileScreen`
**有 ViewModel 的入口層**，負責：
- 訂閱 `viewModel.uiState` 與 `viewModel.profileState`（透過 `collectAsState()`）
- 收集 `viewModel.toastEvent`，以 `Toast.makeText()` 顯示錯誤訊息
- 把狀態與操作傳給 `ProfileScreenContent`

```kotlin
val state by viewModel.profileState.collectAsState()
val uiState by viewModel.uiState.collectAsState()
```

---

### `ProfileScreenContent`
**無狀態的純 UI 層**，接收外部參數驅動畫面。可直接用於 `@Preview`。

| 參數 | 類型 | 說明 |
|------|------|------|
| `uiState` | `ProfileUiState` | 顯示用資料（姓名、ID、邀請碼等） |
| `state` | `ProfileState` | 操作狀態（Idle / Loading / Error） |
| `onLogout` | `() -> Unit` | Logout 按鈕觸發的回呼 |

---

## 畫面區塊

| 區塊（Section Header） | 內容 |
|------------------------|------|
| **Identity** | `IdentityCard`：頭像、姓名、XCash ID、`VerifiedBadge` |
| **Social & Rewards** | `SocialRewardsCard`：邀請碼、Invite Friends 按鈕、Badge 圖示列 |
| **Account** | 個人資料、Recurring、Transaction History |
| **Security** | Security Center |
| **Support** | Help Center、Terms & Conditions |

---

## 子元件說明

### `IdentityCard`
顯示使用者基本資訊：
- 頭像：`CircleShape` 的漸層圓（`NeonPurple` + `NeonCyan`），預設顯示 `Icons.Default.Person`
- 姓名：`uiState.userName`（空值顯示 `---`）
- XCash ID：`uiState.xcashId`（空值顯示 `---`）
- `VerifiedBadge`：`uiState.isVerified == true` 時顯示，紫色漸層膠囊 + CheckCircle icon

### `SocialRewardsCard`
左右分欄佈局（以細線分隔）：
- **左側**：Invite & Earn 標題、`G{inviteCode}` 邀請碼、Invite Friends 按鈕（`onClick` 待接）
- **右側**：My Badges 標題、三個 badge 圖示（`ic_trophy`、`ic_star`、`ic_rocket`）、已獲徽章數

### `ProfileMenuCard`
統一卡片樣式的容器：`neonGlow` + `border` + `CardBackground`，`borderColor` 可自訂。

### `ProfileMenuItem`
選單列表項，接受 `ProfileIcon` 聯合類型（見下方）：

| 參數 | 類型 | 說明 |
|------|------|------|
| `icon` | `ProfileIcon` | 圖示來源（Vector 或 PNG 資源） |
| `label` | `String` | 顯示文字 |
| `iconTint` | `Color` | 圖示顏色，預設 `NeonCyan` |
| `onClick` | `() -> Unit` | 點擊回呼，預設空 |

右側固定顯示 `Icons.Default.ChevronRight`（`alpha = 0.4f`）。

### `LogoutButton`
- `NeonPurple` 背景、`neonGlow` 光暈效果
- `state is ProfileState.Loading` 時 `enabled = false`

---

## ProfileIcon

```kotlin
sealed class ProfileIcon {
    data class Vector(val imageVector: ImageVector) : ProfileIcon()
    data class Resource(@DrawableRes val resId: Int) : ProfileIcon()
}
```

讓 `ProfileMenuItem` 同時支援 `ImageVector`（Material Icons）與 PNG/drawable 資源：

```kotlin
// ImageVector
ProfileMenuItem(icon = ProfileIcon.Vector(Icons.Outlined.Lock), ...)

// PNG mipmap / drawable
ProfileMenuItem(icon = ProfileIcon.Resource(R.mipmap.ic_profile_setting), ...)
```

---

## ProfileUiState

```kotlin
data class ProfileUiState(
    val isLoadingUserInfo: Boolean = true,
    val userName: String = "",
    val xcashId: String = "",
    val inviteCode: String = "G12345",   // 目前硬編碼，待 API 接入
    val badgeCount: Int = 8,             // 目前硬編碼，待 API 接入
    val isVerified: Boolean = true       // 目前硬編碼，待 API 接入
) {
    val isLoading: Boolean get() = isLoadingUserInfo
}
```

> `inviteCode`、`badgeCount`、`isVerified` 尚未從 API 拉取，為預設值佔位。

---

## ProfileState 狀態機

```
Idle
  ↓ logout()
Loading
  ↓ API 成功              ↓ API 失敗
sessionManager            Error（顯示 errorMessage）
  .triggerLogout()
  → 導航回 LoginScreen
```

`ProfileState` 僅用於追蹤 **logout 操作**本身，與頁面資料載入（`uiState.isLoading`）分開。

---

## 資料流向

```
用戶操作
  → ProfileScreenContent（onLogout）
  → ProfileViewModel.logout()
  → AuthRepository.logout()
  → ProfileState（StateFlow）
  → ProfileScreen.collectAsState()
  → 傳入 ProfileScreenContent(state = ...) → UI 更新

init 自動呼叫
  → UserRepository.fetchUserInfo()
  → ProfileUiState（StateFlow）
  → ProfileScreen.collectAsState()
  → 傳入 ProfileScreenContent(uiState = ...) → UI 更新
```

---

## 檔案結構

```
ui/profile/
├── ProfileScreen.kt    # 頁面入口 + ProfileScreenContent + 所有子元件
└── ProfileViewModel.kt # ProfileUiState / ProfileState / ProfileViewModel
```

---

## 注意事項

- `ProfileScreenContent` 是無狀態設計，可直接用於 `@Preview`
- `ProfileMenuItem` 使用 `indication = null` 避免深色背景上出現矩形 ripple 陰影
- `CardBackground` 目前指向 `WelcomeBackground`（`0xFF0E1422`），與頁面底色相同，呈現無邊界融合效果
- 所有 `ProfileMenuItem` 的 `onClick` 目前為空，導航尚未接入
- `SocialRewardsCard` 的 Invite Friends 按鈕 `onClick` 尚未接入
- Badge 圖示直接使用 `R.mipmap.*`，`tint = Color.Unspecified` 保留原始色彩
