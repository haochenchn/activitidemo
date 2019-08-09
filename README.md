# activitidemo
activiti工作流demo，数据库使用mysql，spring + mybatis + activiti

适合入门学习使用，把系统跑起来，流程走一遍，看数据库内表数据的变化。
1. 修改config.properties里面的而数据库连接，然后启动项目，会自动建立activiti库及相应的表；
2. 手动在act_id_user表内录入用户信息；
3. 登录：http://localhost:8080/activitidemo
里面有：
- 模型列表，可创建新模型，发布、导出；
- 流程列表，流程定义，启动流程；
- 运行中流程，正在运行的流程；
4. 调用接口创建模型，模型创建成功后自动跳进流程编辑页面
* `http://localhost:8080/activiti/createModel?name=testname&key=key&description=description111`
5. 编辑好流程后点保存按钮即可。
6. 发布流程接口
* `http://localhost:8080/activiti/deploye`
* 需传入模型id，`{"id":"1"}`
7. 其他接口请看代码。
