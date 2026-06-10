# Navigation 架構說明

## 頁面層級總覽

```
AppNavigation（NavHost，持有唯一 navController）
├── welcome          → WelcomeScreen
├── login            → LoginScreen
├── main             → MainScreen（TabBar 容器）
│   ├── tab 0        → HomeScreen
│   ├── tab 1        → CardsScreen
│   ├── tab 2        → QuestsScreen
│   └── tab 3        → ProfileScreen
│
└── 子頁面（獨立路由，無 TabBar）
    └── security_center → SecurityCenterScreen
        （往後在此擴充）
```

---

## 兩種頁面類型

### 主頁面（Tab 頁面）
由 `MainScreen` 用 `AnimatedContent` 管理，TabBar 始終可見。

| Tab Index | 路由來源 | Screen |
|-----------|----------|--------|
| 0 | `MainScreen` 內部 | `HomeScreen` |
| 1 | `MainScreen` 內部 | `CardsScreen` |
| 2 | `MainScreen` 內部 | `QuestsScreen` |
| 3 | `MainScreen` 內部 | `ProfileScreen` |

- Tab 切換狀態以 `rememberSaveable` 保存，從子頁面返回後維持原 tab
- 主頁面不在 `NavHost` 中登記為獨立路由，統一屬於 `main` 路由

### 子頁面（獨立路由）
在 `NavHost` 中登記為獨立 `composable`，覆蓋整個畫面，TabBar 不顯示。

- 使用自己的 `Scaffold` 處理 statusBar insets（避免標題壓到系統列）
- 必定有 `onBack: () -> Unit` 參數，由 `AppNavigation` 注入 `navController.popBackStack()`
- 進出場使用標準橫向滑動動畫（見下方規範）

---

## 路由常數（`ui/Routes.kt`）

所有路由字串集中定義，禁止在其他檔案直接寫路由字串。

```kotlin
object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val MAIN = "main"

    // Profile sub-pages
    const val SECURITY_CENTER = "security_center"
}
```

新增路由時在對應 section 加一行常數。

---

## 導航 callback 規則

```
AppNavigation
  └── MainScreen( onNavigate = { navController.navigate(it) } )
        └── ProfileScreen( onNavigate = onNavigate )
              └── onClick = { onNavigate(Routes.SECURITY_CENTER) }
```

- `MainScreen` 只持有一個 `onNavigate: (String) -> Unit`，不為個別子頁面新增 callback
- `onNavigate` 沿 tab → Screen 向下傳遞，各 Screen 自行決定路由常數
- 新增子頁面時 `MainScreen` **不需要修改**

---

## 標準進出場動畫

子頁面統一採用橫向滑動：

| 參數 | 設定 | 說明 |
|------|------|------|
| `enterTransition`（子頁面） | `slideInHorizontally { it }` | 從右滑入 |
| `popExitTransition`（子頁面） | `slideOutHorizontally { it }` | 向右滑出（返回） |
| `exitTransition`（main，目標為子頁面） | `slideOutHorizontally { -it }` | 向左滑出 |
| `popEnterTransition`（main，來源為子頁面） | `slideInHorizontally { -it }` | 從左滑入（返回） |

`exitTransition` / `popEnterTransition` 在 `main` 路由使用 `when (route)` 判斷，只對特定子頁面生效：

```kotlin
composable(
    route = Routes.MAIN,
    exitTransition = {
        when (targetState.destination.route) {
            Routes.SECURITY_CENTER -> slideOutHorizontally { -it }
            else -> null
        }
    },
    popEnterTransition = {
        when (initialState.destination.route) {
            Routes.SECURITY_CENTER -> slideInHorizontally { -it }
            else -> null
        }
    }
)
```

---

## 新增子頁面步驟

以新增 `ProfileEditScreen` 為例：

### Step 1 — `Routes.kt`
```kotlin
// Profile sub-pages
const val SECURITY_CENTER = "security_center"
const val PROFILE_EDIT = "profile_edit"   // ← 新增
```

### Step 2 — `AppNavigation.kt`（子頁面路由）
```kotlin
composable(
    route = Routes.PROFILE_EDIT,
    enterTransition = { slideInHorizontally { it } },
    popExitTransition = { slideOutHorizontally { it } }
) {
    ProfileEditScreen(onBack = { navController.popBackStack() })
}
```

### Step 3 — `AppNavigation.kt`（main 路由補動畫）
```kotlin
exitTransition = {
    when (targetState.destination.route) {
        Routes.SECURITY_CENTER -> slideOutHorizontally { -it }
        Routes.PROFILE_EDIT    -> slideOutHorizontally { -it }   // ← 新增
        else -> null
    }
},
popEnterTransition = {
    when (initialState.destination.route) {
        Routes.SECURITY_CENTER -> slideInHorizontally { -it }
        Routes.PROFILE_EDIT    -> slideInHorizontally { -it }   // ← 新增
        else -> null
    }
}
```

### Step 4 — Screen 內呼叫
```kotlin
ProfileMenuItem(
    onClick = { onNavigate(Routes.PROFILE_EDIT) }
)
```

### Step 5 — 建立 Screen 檔案與資料夾結構

每個子頁面建立獨立資料夾，該頁面專屬的 Compose UI 元件放到該資料夾下的 `components/` 子資料夾：

```
ui/profile/
└── edit/
    ├── ProfileEditScreen.kt     # 有自己的 Scaffold，接受 onBack: () -> Unit
    └── components/              # ProfileEditScreen 專屬元件（如有）
        └── SomeEditComponent.kt
```

多個子頁面共用的元件（如 ViewModel 共用的 Dialog）則放在父資料夾的 `components/`：

```
ui/scanpay/
├── ScanPayViewModel.kt          # 整個 flow 共用，放根目錄
├── ScanPayScreen.kt             # Tab 主頁面，放根目錄
├── components/                  # ScanPayScreen / flow 共用元件
│   ├── MyQrContent.kt
│   ├── SelectSplitPartnerDialog.kt
│   └── SplitPartnersRow.kt
├── input/
│   └── InputAmountScreen.kt
├── confirm/
│   └── ConfirmPaymentScreen.kt
└── success/
    └── TransactionSuccessfulScreen.kt
```

**package 命名規則**：跟隨資料夾路徑
- `ui/scanpay/confirm/ConfirmPaymentScreen.kt` → `package com.example.newproject.ui.scanpay.confirm`
- `ui/scanpay/components/MyQrContent.kt` → `package com.example.newproject.ui.scanpay.components`

**import 規則**：不同 package 間的引用必須補 `import`，不可依賴同 package 可見性

---

## 相關檔案對照

| 檔案 | 職責 |
|------|------|
| `ui/Routes.kt` | 所有路由字串常數 |
| `ui/AppNavigation.kt` | NavHost 定義、路由 → Screen 對應、進出場動畫 |
| `ui/main/MainScreen.kt` | TabBar 容器，持有 `onNavigate` 並向下傳遞 |
| `ui/profile/ProfileScreen.kt` | 接收 `onNavigate`，各 MenuItem 觸發導航 |
| `docs/screens/` | 各頁面的詳細說明文件 |
