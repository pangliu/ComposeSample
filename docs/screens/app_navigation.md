# AppNavigation

## 職責

`AppNavigation` 是 App 層級的 **Navigation Graph**，負責：

- 定義所有頂層 route 與對應的 Composable
- 監聽全局登出事件（`appViewModel.logoutEvent`），觸發時強制跳回 `"login"` 並清空 back stack
- 不持有任何業務資料，只做導向

---

## 檔案對應

| 檔案 | 說明 |
|------|------|
| `ui/AppNavigation.kt` | Navigation graph 主體 |
| `ui/AppViewModel.kt` | 提供 `logoutEvent` SharedFlow |

---

## Route 對照表

| Route | Composable | 說明 |
|-------|-----------|------|
| `"welcome"` | `WelcomeScreen` | startDestination；自動登入或跳轉決策 |
| `"login"` | `LoginScreen` | 手動登入；成功後導向 `"main"` |
| `"main"` | `MainScreen` | App 主體（含底部導覽列與所有 tab） |

---

## 導航流程

```
App 啟動
  └── welcome
        ├── 已有 Token → navigate("main") { popUpTo("welcome") inclusive }
        └── 無 Token   → navigate("login")  { popUpTo("welcome") inclusive }

login 成功
  └── navigate("main") { popUpTo("login") inclusive }

全局登出（401 / 1001 / 1005）
  └── logoutEvent → navigate("login") { popUpTo(0) inclusive }
      （若 currentDestination 已是 "login" 則略過，避免重建）
```

---

## 全局登出機制

`LaunchedEffect(Unit)` 在 `AppNavigation` 生命週期內持續收集 `appViewModel.logoutEvent`：

```kotlin
LaunchedEffect(Unit) {
    appViewModel.logoutEvent.collect {
        if (navController.currentDestination?.route != "login") {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
```

- `popUpTo(0) { inclusive = true }` — 清空整個 back stack，確保按返回鍵不會回到登入前的畫面
- 錯誤碼攔截由 `BaseRepository.handleGlobalError()` 負責，觸發後 emit `logoutEvent`；各 ViewModel **不需要**重複處理這些錯誤碼

---

## 新增頂層 Route 步驟

1. 在 `NavHost` 內新增 `composable("route_name") { YourScreen() }`
2. 在需要導向的畫面呼叫 `navController.navigate("route_name")`
3. 視需要加上 `popUpTo` 管理 back stack
4. 更新本文件的 Route 對照表
