# ComposeSample — Project Guide for Claude

## Overview

Android app built with Jetpack Compose. Currently a feature prototype / sandbox project.

- **Package**: `com.example.newproject`
- **Min SDK**: 26 | **Target SDK**: 35
- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3

---

## Tech Stack

| 分類 | 技術 |
|------|------|
| UI | Jetpack Compose, Material3 |
| DI | Hilt (`@HiltViewModel`, `@Singleton`, `@Inject`) |
| Async | Kotlin Coroutines + StateFlow |
| Network | Retrofit2 + OkHttp + Moshi |
| Persistence | SharedPreferences (`"app_prefs"`) |
| Navigation | Compose Navigation |

---

## Project Structure

```
app/src/main/java/com/example/newproject/
├── di/
│   ├── NetworkModule.kt        # Hilt DI — Retrofit / OkHttp providers
│   └── NetworkQualifiers.kt   # @PublicClient / @AuthClient qualifiers
├── network/
│   ├── AuthInterceptor.kt     # 自動附加 JWT Token 的 OkHttp Interceptor
│   ├── manager/
│   │   ├── TokenManager.kt    # JWT token 存取（SharedPreferences）
│   │   ├── EssentialsManager.kt  # My Menu 排序持久化（SharedPreferences）
│   │   └── SessionManager.kt
│   ├── api/                   # Retrofit interface 定義
│   ├── fake/                  # 開發用假資料實作（取代真實 API）
│   └── model/
│       └── response/
│           ├── UserInfoResponse.kt        # 使用者資訊（含 empty() companion）
│           └── OrderHistoryResponse.kt    # 訂單歷史（OrderType / OrderStatus enums）
├── repository/
│   ├── BaseRepository.kt      # safeApiCall + handleGlobalError（401/1001/1005 → logout）
│   ├── AuthRepository.kt
│   └── UserRepository.kt      # fetchUserInfo / fetchOrderHistory
├── ui/
│   ├── AppNavigation.kt       # Navigation graph（route "main" → MainScreen）
│   ├── AppViewModel.kt
│   ├── main/
│   │   ├── MainScreen.kt      # 導覽容器：Scaffold + AnimatedContent tab 切換
│   │   └── nvaTab/
│   │       └── CustomBottomNavigation.kt  # 底部導覽列（含 ScanAndPayTab overlay）
│   ├── UiEvent.kt             # 共用一次性事件 sealed class（ShowToast / ShowDialog）
│   ├── home/
│   │   ├── HomeScreen.kt      # 自行注入 HomeViewModel；純內容頁面
│   │   ├── HomeViewModel.kt   # HomeUiState（純資料容器）+ eventFlow SharedFlow
│   │   ├── balance/
│   │   │   └── BalanceCard.kt
│   │   ├── quests/
│   │   │   └── QuestCard.kt
│   │   ├── recent/
│   │   │   └── RecentActivity.kt  # 接收 List<OrderHistoryResponse>，純 UI
│   │   └── essential/
│   │       ├── EssentialItems.kt      # EssentialItem data class + allEssentialItems 清單
│   │       ├── XEssentialsCard.kt     # Home 頁快捷功能卡片（HorizontalPager）
│   │       └── EditEssentialsDialog.kt # 拖曳排序 Dialog
│   ├── login/
│   ├── welcome/
│   ├── components/
│   └── theme/
│       └── Color.kt           # 專案色彩定義
└── utils/
    └── DeviceInfoProvider.kt
```

---

## Architecture

**MVVM + Repository pattern**

```
Composable → ViewModel (StateFlow) → Repository → API / Manager
```

- Composable 只讀 StateFlow，不直接呼叫 Repository
- ViewModel 負責業務邏輯與狀態管理
- Repository 負責資料來源切換（真實 API / Fake）

---

## 重要慣例

### DI
- 所有 ViewModel 使用 `@HiltViewModel` + `@Inject constructor`
- Singleton 服務使用 `@Singleton` + `@Inject constructor`
- 需要 `Context` 時使用 `@ApplicationContext`

### Network
- 有兩組 Retrofit：`@PublicClient`（無 Token）和 `@AuthClient`（有 Token）
- 目前 API 皆使用 **Fake 實作**（`FakeUserApiService` 等），切換到真實 API 只需在 `NetworkModule.kt` 註解切換

### Persistence
- 所有持久化統一使用 `SharedPreferences` 檔案名稱 `"app_prefs"`
- `TokenManager`：JWT token
- `EssentialsManager`：My Menu 排序（儲存 label 字串，讀取時 map 回 `allEssentialItems`）

### EssentialItem
- `EssentialItem` 含 `ImageVector`，無法序列化
- 持久化只存 `label`，讀取時從 `allEssentialItems` 反查完整物件

### Compose
- 避免在 Composable 內直接使用 `BoxWithConstraints`，改用 `Box + onSizeChanged`
- `clickable` 在深色背景上需加 `indication = null` 避免長按時出現矩形 ripple 陰影
- Dialog 動畫使用 `AnimatedVisibility` + `slideInVertically`/`fadeIn`

### TopBar / 標題高度規範

| 頁面類型 | 結構 | fontSize | 高度來源 |
|----------|------|----------|----------|
| **Tab 主頁面**（Home / Cards / Quests / Profile） | 純 `Text` + padding | `20.sp` Bold | `padding(top = 24.dp, bottom = 16.dp)` |
| **子頁面**（含返回鍵） | `Box(height = 56.dp)` + 標題置中 | `20.sp` Bold | `height = 56.dp` |

**Tab 主頁面標題標準寫法：**
```kotlin
Text(
    text = stringResource(R.string.xxx_title),
    color = Color.White,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp, bottom = 16.dp),
    textAlign = TextAlign.Center
)
```

**注意事項：**
- Tab 主頁面標題列若需放置圖示按鈕（如 HomeScreen 的通知/設定），禁止使用 `IconButton`
- `IconButton` 強制最小觸控區域為 **48dp**，會撐高整個 Row 造成標題高度不一致
- 改用 `Icon + clickable(indication = null)` 並設定 `Modifier.size(24.dp)` 自行控制大小

### Network / API
- 所有 API 呼叫統一透過 `safeApiCall {}` 包裝，回傳 `NetworkResult<T>`
- ViewModel 必須用 `when (result)` 處理三種分支：`Success`、`Error`、`Exception`
- 新增 API 時同步在對應的 `Fake*ApiService` 加入模擬實作（含 `delay()` 模擬延遲）
- 需要 Token 的 API 放 `UserApiService`（`@AuthClient`），公開 API 放 `PublicApiService`（`@PublicClient`）
- Repository 只在 `NetworkResult.Success` 時執行後續本地操作（如存 token、清 token）
- `BaseRepository.handleGlobalError()` 已攔截 401/1001/1005 並觸發 logout，ViewModel **不需要**再重複檢查這些錯誤碼

### State vs Event 設計原則

兩種概念用不同的 Flow 承載，**不可混用**：

| | State（持續） | Event（一次性） |
|---|---|---|
| Flow 類型 | `StateFlow` | `SharedFlow` |
| sealed class 用法 | ❌ 禁止 | ✅ 適合 |
| 例子 | `isLoading`、卡片清單 | ShowToast、導航到某頁 |

### UiState 設計
- UiState 是**純資料容器**（data fields + `isLoading`），不使用 sealed class 狀態機
- 每隻 API 對應一個 `isLoadingX: Boolean = true` 欄位；`isLoading` 計算屬性 = 所有旗標的 OR
- 新增 API 只需在 UiState 加一個 boolean 旗標，`isLoading` 自動包含（保持擴充彈性）
- 資料缺失 → 使用空預設值（`UserInfoResponse.empty()`、`emptyList()`），不進入錯誤 UI 狀態

### UiEvent 設計
- API 錯誤 → ViewModel 透過 `_eventFlow.emit(UiEvent.ShowToast(message))` 通知
- 需要彈窗 → `_eventFlow.emit(UiEvent.ShowDialog(title, message))`
- `_eventFlow` 宣告為 `MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)`，避免 emit 被丟棄
- Composable 用 `LaunchedEffect(Unit)` 收集，用 `when (event)` 分支處理：
  ```kotlin
  LaunchedEffect(Unit) {
      viewModel.eventFlow.collect { event ->
          when (event) {
              is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
              is UiEvent.ShowDialog -> { /* 顯示 AlertDialog */ }
          }
      }
  }
  ```

### NavigationEvent 設計（Login 等多步驟流程）
- 導航事件（Success、NeedsVerification、PromptBiometricEnroll）用獨立的 `LoginNavigationEvent` sealed class + `SharedFlow`
- 這些事件屬於「一次性觸發導航/彈窗」，與 UiEvent 分開宣告，不混入 UiState
- Screen 層用 `LaunchedEffect(Unit) { viewModel.navigationEvent.collect { ... } }` 收集，更新 local state 或直接呼叫導航

### 新增 API 步驟（以 HomeScreen 為例）
1. **`UserApiService`**：新增 `@GET suspend fun xxx(): BaseResponse<T>`
2. **`FakeUserApiService`**：實作相同 function，加 `delay()` 模擬延遲，回傳假資料
3. **`UserRepository`**：新增 `suspend fun fetchXxx(): NetworkResult<T>` 包裝 `safeApiCall`
4. **`HomeUiState`**：新增 `val isLoadingXxx: Boolean = true` 與資料欄位
5. **`HomeViewModel`**：`init` 中呼叫新 function；用 `when (result)` 更新 `_uiState`，錯誤用 `_eventFlow.emit(UiEvent.ShowToast(...))` 通知
6. **Composable**：從 `uiState` 取出資料傳入子元件，不自行處理 loading/error 狀態

### Navigation — 子頁面（無 TabBar）

主要頁面（Home / Cards / Quests / Profile）由 `MainScreen` 以 tab 切換管理，TabBar 始終可見。  
**子頁面**（如 SecurityCenter）是獨立的 `NavHost` 路由，TabBar 不顯示。

#### 路由常數
所有路由字串集中定義在 `ui/Routes.kt`：

```kotlin
object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val MAIN = "main"
    // Profile sub-pages
    const val SECURITY_CENTER = "security_center"
}
```

新增路由時只需在對應 section 下加一行常數，禁止在其他檔案直接寫路由字串。

#### 新增子頁面步驟

1. **`Routes.kt`**：在對應區塊新增常數
   ```kotlin
   const val PROFILE_EDIT = "profile_edit"
   ```

2. **`AppNavigation.kt`**：新增 `composable`，加入進出場動畫
   ```kotlin
   composable(
       route = Routes.PROFILE_EDIT,
       enterTransition = { slideInHorizontally { it } },
       popExitTransition = { slideOutHorizontally { it } }
   ) {
       ProfileEditScreen(onBack = { navController.popBackStack() })
   }
   ```
   同時在來源路由（通常是 `Routes.MAIN`）補上對應的 `exitTransition` / `popEnterTransition`：
   ```kotlin
   exitTransition = {
       when (targetState.destination.route) {
           Routes.PROFILE_EDIT -> slideOutHorizontally { -it }
           ...
       }
   }
   ```

3. **在 Screen 內呼叫導航**：透過已傳入的 `onNavigate` lambda，不需修改 `MainScreen`
   ```kotlin
   onClick = { onNavigate(Routes.PROFILE_EDIT) }
   ```

4. **子頁面本身**：使用自己的 `Scaffold` 處理 statusBar insets，`onBack` 由外部注入

#### 導航 callback 規則
- `MainScreen` 只持有一個 `onNavigate: (String) -> Unit` 參數，不針對個別子頁面新增 callback
- `onNavigate` 沿 tab → Screen 向下傳遞，各 Screen 自行決定要導向哪個路由
- Tab 切換狀態用 `rememberSaveable` 保存，從子頁面返回後仍停留在原來的 tab

#### 標準進出場動畫
| 情境 | 動畫 |
|------|------|
| 進入子頁面 | `slideInHorizontally { it }`（從右） |
| 返回上一頁 | `slideOutHorizontally { it }`（向右） |
| 上層頁面被推走 | `slideOutHorizontally { -it }`（向左） |
| 上層頁面返回 | `slideInHorizontally { -it }`（從左） |

---

### 子頁面間資料傳遞規則

依資料性質選擇對應方式，**禁止混用**：

| 情境 | 方式 |
|------|------|
| 進入 flow 入口時帶入少量初始資料（ID、名稱） | **Nav Argument**（query param） |
| 同一個 flow 內多個頁面共享 / 讀寫資料 | **共享 ViewModel**（Nested NavGraph scoped） |
| 子頁面完成後通知上層（返回、觸發導航） | **Lambda Callback**（`onBack`、`onNavigate`） |
| 多個欄位的物件跨頁面傳遞 | **ViewModel**（禁止序列化塞入路由字串） |

#### Flow ViewModel 設計（Nested NavGraph）

需要跨多個子頁面共享資料時，建立巢狀 NavGraph 並 scope ViewModel 到該 graph：

```kotlin
// Routes.kt
const val SCAN_PAY_FLOW = "scan_pay_flow"
const val SCAN_PAY_INPUT_AMOUNT = "scan_pay_input_amount?username={username}&name={name}"
fun inputAmount(username: String = "", name: String = "") =
    "scan_pay_input_amount?username=${android.net.Uri.encode(username)}&name=${android.net.Uri.encode(name)}"

// AppNavigation.kt
navigation(startDestination = Routes.SCAN_PAY_INPUT_AMOUNT, route = Routes.SCAN_PAY_FLOW) {
    composable(Routes.SCAN_PAY_INPUT_AMOUNT, arguments = ...) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Routes.SCAN_PAY_FLOW)
        }
        val viewModel = hiltViewModel<ScanPayViewModel>(parentEntry)  // 同一個 instance
        InputAmountScreen(viewModel = viewModel, ...)
    }
    composable(Routes.SCAN_PAY_CONFIRM_PAYMENT) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Routes.SCAN_PAY_FLOW)
        }
        val viewModel = hiltViewModel<ScanPayViewModel>(parentEntry)  // 同一個 instance
        ConfirmPaymentScreen(viewModel = viewModel, ...)
    }
}
```

#### Flow 入口的初始資料

flow 入口（startDestination）可透過 **optional query param** 接收上層帶入的資料，進入後立即存入 ViewModel：

```kotlin
// 入口 Composable（AppNavigation 中）
val username = backStackEntry.arguments?.getString("username") ?: ""
InputAmountScreen(viewModel = viewModel, recipientUsername = username, ...)

// Screen 本身用 LaunchedEffect 存入 ViewModel（只跑一次）
LaunchedEffect(recipientUsername) {
    viewModel.setRecipientInfo(recipientUsername, recipientName)
}
```

Flow 內的後續頁面（如 ConfirmPaymentScreen）直接從 `viewModel.uiState.collectAsState()` 讀取，**不再透過 nav arg 傳遞**。

#### 現有 Flow 對照
| Flow | Route 常數 | ViewModel |
|------|-----------|-----------|
| ScanPay 付款流程 | `SCAN_PAY_FLOW` | `ScanPayViewModel` |

---

### 字串管理
- 所有 UI 顯示字串（Text、contentDescription 等）必須寫入 `app/src/main/res/values/strings.xml`
- Composable 中使用 `stringResource(R.string.xxx)` 引用，禁止硬編碼字串
- `strings.xml` 依頁面加上區塊註解（例如 `<!-- HomeScreen -->`），新增字串時放到對應頁面的區塊下
- 格式化字串使用 `%s`（字串）/ `%d`（整數）佔位符，搭配 `stringResource(R.string.xxx, arg)`

---

## Color Palette（主要色彩）

| 名稱 | 用途 |
|------|------|
| `WelcomeBackground` | 主背景色 |
| `DarkBackground` | 深色背景 |
| `NeonCyan` | 主要強調色 |
| `NeonPurple` | 次要強調色 |
| `CashInGreen` | 入帳 / 確認操作 |
| `SendPink` | 出帳 / 移除操作 |
| `CardGradientStart/Mid/End` | 卡片漸層 |
