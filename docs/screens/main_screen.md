# MainScreen 頁面結構說明

## 職責

`MainScreen` 是 App 主體的**導覽容器**，職責單一：

- 持有底部導覽列（`CustomBottomNavigation`）
- 根據 `selectedIndex` 切換顯示對應的子頁面
- **不擁有任何業務資料**，不注入子頁面的 ViewModel

子頁面各自管理自己的 ViewModel 與 API，`MainScreen` 完全不感知。

---

## 檔案對應

| 檔案 | 說明 |
|------|------|
| `ui/AppNavigation.kt` | Navigation graph（route 定義） |
| `ui/main/MainScreen.kt` | 導覽容器主體 |
| `ui/main/nvaTab/CustomBottomNavigation.kt` | 底部導覽列元件 |

---

## Composable 層級樹

```
MainScreen()
└── Scaffold
    ├── bottomBar: CustomBottomNavigation(selectedIndex, onTabSelected)
    └── content: AnimatedContent(selectedIndex)
          └── Box (fillMaxSize)
              ├── index=0 → HomeScreen()      ← 自己注入 HomeViewModel
              ├── index=1 → CardsScreen()
              ├── index=2 → QuestsScreen()
              └── index=3 → ProfileScreen(profileViewModel)
```

---

## Tab 切換動畫

`AnimatedContent` 根據 index 變化方向決定滑動方向：

```kotlin
transitionSpec = {
    if (targetState > initialState) {
        slideInHorizontally { it } togetherWith slideOutHorizontally { -it }   // 向左
    } else {
        slideInHorizontally { -it } togetherWith slideOutHorizontally { it }   // 向右
    }
}
```

---

## CustomBottomNavigation

**位置**：`ui/main/nvaTab/CustomBottomNavigation.kt`  
**高度**：固定 60dp（`NavBarHeight`）

```
Box (fillMaxWidth, height 60dp, contentAlignment = BottomCenter)
├── Row (background WelcomeBackground, SpaceEvenly)
│   ├── BottomNavItem (weight 1f) → Home    (index 0)
│   ├── BottomNavItem (weight 1f) → Cards   (index 1)
│   ├── Spacer         (weight 1f)          ← 中間佔位（給 ScanAndPayTab overlay 用）
│   ├── BottomNavItem (weight 1f) → Quests  (index 2)
│   └── BottomNavItem (weight 1f) → Profile (index 3)
└── ScanAndPayTab()  ← overlay 在 Box 底部正中央
```

### ScanAndPayTab 設計重點

- `wrapContentHeight(unbounded = true)`：Column 突破父層 60dp 限制向上延伸，背景圖自然超出 nav bar
- `ContentScale.FillWidth`：背景圖維持原始比例，不被裁切
- `padding(bottom = 8.dp)`：與 `BottomNavItem` 相同，確保文字底部對齊

---

## Preview 說明

`MainScreen` 內的子頁面依賴 Hilt，無法在 Preview 中直接渲染。  
`MainScreenPreview` 只預覽導覽外殼（Scaffold + CustomBottomNavigation），內容區域為空白：

```kotlin
@Preview(showBackground = true)
private fun MainScreenPreview() {
    // 只顯示 Scaffold + nav bar，不渲染子頁面
}
```

子頁面的 Preview 各自定義在自己的檔案中（如 `HomeScreenPreview`）。

---

## 新增底部 Tab 步驟

### Step 1 — 在 `CustomBottomNavigation` 加入新 Tab 項目

在 `Row` 內新增一個 `BottomNavItem`，並調整 `Spacer` 位置（保持中間掃碼鍵置中）：

```kotlin
BottomNavItem(
    modifier = Modifier.weight(1f),
    defaultIconRes = R.mipmap.ic_new_tab,
    activeIconRes = R.mipmap.ic_new_tab_active,
    title = stringResource(R.string.home_nav_new_tab),
    isSelected = selectedIndex == 4,
    onClick = { onTabSelected(4) }
)
```

### Step 2 — 在 `MainScreen` 的 `when` 新增路由

```kotlin
when (index) {
    0 -> HomeScreen()
    1 -> CardsScreen()
    2 -> QuestsScreen()
    3 -> { val vm: ProfileViewModel = hiltViewModel(); ProfileScreen(vm) }
    4 -> NewTabScreen()   // ← 新增
}
```

### Step 3 — 在 `strings.xml` 新增 Tab 標籤字串

```xml
<string name="home_nav_new_tab">New Tab</string>
```

### Step 4 — 準備新 Tab 的 icon 資源

將 `ic_new_tab.webp` 與 `ic_new_tab_active.webp` 放入對應的 `mipmap-*` 資料夾。

---

## 架構原則

| 原則 | 說明 |
|------|------|
| `MainScreen` 不注入子頁面 ViewModel | 各頁面自行 `hiltViewModel()`，MainScreen 只負責切換 |
| `MainScreen` 不傳遞業務資料給子頁面 | 子頁面完全自給自足，可獨立重用 |
| 新增 Tab 不影響既有頁面 | `when (index)` 擴充，不修改其他分支 |
