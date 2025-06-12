# 現代 Android 多模組化實戰課程心得整理

## 1. 可以學到什麼
- 如何使用 Kotlin DSL（kts）整合與管理 Gradle 設定
- 多模組（Modularization）架構設計與實作
- 使用 buildSrc 統一管理依賴與版本
- Flavors、Build Types、Dimensions 的進階應用
- 測試依賴與環境分離
- 實作 DataStore、MVVM 架構、網路層封裝
- 自訂 Gradle Plugin 的基礎

## 2. 每個章節大約講什麼
1. **第一章**：課程介紹、適合對象、章節預覽、Github 連結
2. **第二章**：建立 buildSrc，Kotlin DSL 管理 Gradle 設定與依賴，集中管理 dependencies、test dependencies、build config
3. **第三章**：介紹 Flavors、Build Types、Dimensions，並用 Kotlin 封裝這些設定，靈活切換不同打包組合
4. **第四章**：建立 features 模組（如 Login、Home），處理 module gradle 設定與依賴管理，導入 Room、Hilt 等
5. **第五章**：建立共用 Gradle Plugin，模組化 gradle 設定，導入 KTLint、Spotless 等自動化工具
6. **第六章**：新增 core、data、domain、presentation 等基礎模組，導入 Hilt、JavaPoet
7. **第七章**：data module 實作 http intercept、header intercept、OkHttpClient provider，並用 Hilt 注入
8. **第八章**：實作 Login module，建立 data/domain/presentation 架構，設計 LoginService 與 DI
9. **第九章**：data module 實作網路狀態監控、NetworkDataSource、通用 OutCome/UseCase 封裝、錯誤處理
10. **第十章**：Login module 實作 Remote、DI、Mapper、Coroutine Dispatcher，並處理 DI 問題
11. **第十一章**：引入 DataStore、kotlinx-collections-immutable，設計 AppSettings、Serializer，並測試 UI
12. **第十二章**：介紹 Preferences DataStore 與 Proto DataStore 差異，實作 proto schema、Serializer、Manager 封裝
13. **第十三章**：介紹 Chucker API 攔截器，實作 AuthenticationIntercept、ConnectivityInterceptor，並處理 token 驗證
14. **第十四章**：data module 新增 Mapper，調整三層架構（Domain/Data/Presentation），設計 UseCase 抽象層
15. **第十五章**：Login module 新增 presentation 層，設計 ViewModel、ViewState、Validator、UI 與 DI
16. **第十六章**：Demo 登入流程，設計 StateRenderer 通用 UI 狀態封裝，Compose UI 狀態管理
17. **第十七章**：導入 navigation module，設計 AppNavigator、NavigationDestination、Screens，並實作 Home/Signup module 與 UI 跳轉

## 3. 適合觀看的人
- 想學習現代 Android 多模組化架構的開發者
- 想了解 Gradle kts 整合與管理 Gradle 設定
- 建議有幾年 Android 經歷，並對 Compose、Hilt、Retrofit、Room、Navigate 有基礎知識者

## 4. 缺點
- 內容偏重於架構與設定，對於 UI/UX 或實際業務邏輯較少著墨
- 需要有一定 Kotlin 與 Gradle 基礎，初學者可能較難跟上
- 部分新技術（如 toml、最新 Gradle 版本）未深入比較
- 章節之間有時跳躍較快，需自行補充相關知識 