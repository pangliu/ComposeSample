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
│   └── model/                 # request / response data class
├── repository/
│   ├── BaseRepository.kt
│   ├── AuthRepository.kt
│   └── UserRepository.kt
├── ui/
│   ├── AppNavigation.kt       # Navigation graph
│   ├── AppViewModel.kt
│   ├── home/
│   │   ├── HomeScreen.kt      # Home UI + Bottom Navigation
│   │   ├── HomeViewModel.kt   # homeState + myMenuItems StateFlow
│   │   ├── BalanceCard.kt
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

### Network / API
- 所有 API 呼叫統一透過 `safeApiCall {}` 包裝，回傳 `NetworkResult<T>`
- ViewModel 必須用 `when (result)` 處理三種分支：`Success`、`Error`、`Exception`
- 新增 API 時同步在對應的 `Fake*ApiService` 加入模擬實作（含 `delay()` 模擬延遲）
- 需要 Token 的 API 放 `UserApiService`（`@AuthClient`），公開 API 放 `PublicApiService`（`@PublicClient`）
- Repository 只在 `NetworkResult.Success` 時執行後續本地操作（如存 token、清 token）

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
