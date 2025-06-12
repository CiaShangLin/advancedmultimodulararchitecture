# 現代 Android 多模組化實戰課程心得整理

教學影片網址：[@ Udemy 課程連結](https://www.udemy.com/course/mastering-multi-modular-clean-architecture-in-android/learn/lecture/45680117#overview)
教學影片的 GitHub：[@ minafarideleia/AdvancedMultiModularArchitecture](https://github.com/minafarideleia/AdvancedMultiModularArchitecture)

## 個人觀後心得
對於模組化各個層面上有了一定基礎上的了解，和編寫BuildSrc使用gradle.kts去整合各模組間的gradle依賴設定，整體來說對於模組化哪一層該依賴於哪一層，那一些東西該放在哪一層有了一定程度上的了解，在還沒看之前看過Now Android，當時就對她的core module，data module該怎麼去區分感到疑惑，而在Features Module該引入那些Module是一知半解。
看完這個教學之後，雖然很難說在目前公司的專案上實現，但是在Flavor和QA則是可以導入到公司目前的專案使用，可以簡化QA測試時流程和步驟，自動化的設定，例如:ktlint的導入也可以有效的管理不同裝置排版和多開發者的習慣。

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
- 看了Now Android後一頭霧水的
- 建議有幾年 Android 經歷，並對 Compose、Hilt、Retrofit、Room、Navigate 有基礎知識者

## 4. 小缺點
- 講師口音在開1.5倍速的時候聽不太清楚，只有英文字幕
- 最後幾張繪製UI的時候有點水時間
- 有幾層沒有詳細實作，例如:Room(Locale Data)，DataSource，Mapper之類的

