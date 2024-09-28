# lime-japi-docs

![输入图片说明](v-images/logo.jpg)

## 1 简介

lime-japi-docs是一个简单的Java接口文档生成工具，通过解析Java源码及其注释生成Controller接口文档，可在网页端管理、生成和浏览文档。项目打包为独立可执行jar包，对解析的源码零侵入，直接运行后在网页上进行初始化和配置即可。若只想解析获取Controller源码接口数据，只需要在自己项目中引入核心解析包「<a href="https://gitee.com/xuchenoak/lime-japi-docs-parser" target="_blank">lime-japi-docs-parser</a>」进行解析即可，核心包已开源并上传至maven中央仓库。

运行环境：JDK1.8+

## 2 下载

- 下载地址：<a href="https://gitee.com/xuchenoak/lime-japi-docs/releases" target="_blank">点击下载</a>


- 体验地址：<a href="https://ljd.xuchenoak.com" target="_blank">在线体验</a>

    邀请码：666  管理登录账/密：admin/123456


- 页面截图：

| ![输入图片说明](v-images/01.init.png)                 | ![输入图片说明](v-images/02.visit_code.png)             |
|-------------------------------------------------|---------------------------------------------------|
| ![输入图片说明](v-images/03.no_docs_and_no_login.png) | ![输入图片说明](v-images/04.login.png)                  |
| ![输入图片说明](v-images/05.login_suc.png)            | ![输入图片说明](v-images/06.sys_config.png)             |
| ![输入图片说明](v-images/07.add_doc.png)              | ![输入图片说明](v-images/08.add_doc_path.png)           |
| ![输入图片说明](v-images/08.01.param_valid.png)       | ![输入图片说明](v-images/08.02.field_default_value.png) |
| ![输入图片说明](v-images/09.add_doc_suc.png)          | ![输入图片说明](v-images/10.parse_doc.png)              |
| ![输入图片说明](v-images/11.parse_doc_suc.png)        | ![输入图片说明](v-images/12.doc_get.png)                |
| ![输入图片说明](v-images/13.doc_post.png)             | ![输入图片说明](v-images/14.res_json.png)               |
| ![输入图片说明](v-images/15.res_jsobj.png)            | ![输入图片说明](v-images/16.parse_version.png)          |

  - 2024-09-28 v1.1.2更新截图

| ![输入图片说明](v-images/search.png) | ![输入图片说明](v-images/docs_key.png) |
|--------------------------------|----------------------------------|
| ![输入图片说明](v-images/router.png) |                                  |

## 3 使用

### 3.1 运行服务

进入「<a href="https://gitee.com/xuchenoak/lime-japi-docs/releases" target="_blank">下载</a>」页面下载最新版本的zip包，然后将压缩包解压，在java环境下`java -jar lime-japi-docs-版本号.jar`直接运行目录下的jar包，启动后访问 http://127.0.0.1:3001 即可进行初始化并开始配置生成文档了！
1. 建议下载`lime-japi-docs-1.1.*.zip`的最新版本，`lime-japi-docs-1.0.1.zip`版本比较粗糙，感兴趣的话需要找到对应版本标签里的`README.md`；
2. jar包目录下贴心提供了`restart.cmd`（重启）和`stop.cmd`（关闭）服务的脚本，默认端口3001，如果使用其它端口启动记得脚本里也改一下；
3. jar包目录下会生成数据库文件`./data/data.mv.db`（所有配置都在数据库里，数据库删除了就要重新配置和生成文档了）和日志文件`./data/log`（想看日志自己查）。

### 3.2 源码注释示例（就是最最普通的java注释啦）

1. Controller及接口方法注释示例：

```java

/**
 * 用户管理业务接口
 **/
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

   /**
    * 获取用户列表
    * @param username 用户名（搜索）
    * @param age 年龄（搜索）
    * @param sex 性别（搜索）
    * @return
    */
   @GetMapping("/list")
   public AjaxResult<List<UserVo>> list(String username, Integer age, String sex) {
      // ……
      return AjaxResult.suc();
   }

   /**
    * 新增用户
    * @return
    */
   @PostMapping("/add")
   public AjaxResult<UserVo> add(@Validated @RequestBody UserAddRf rf) {
      // ……
      return AjaxResult.suc();
   }

   // ……

}
```

2. 接口方法入参类注释示例：

   参数验证注释可通过文档配置的回调对如 `@NotBlank`、`@NotNull`等注解进自定义注释。

```java
/**
 * 用户信息入参
 **/
@Data
public class UserAddRf {

   /** 用户名 */
   @NotBlank(message = "用户名不能为空")
   private String username;

   /** 年龄 */
   @NotNull(message = "用户年龄不能为空")
   private Integer age;

   /** 性别 */
   @NotBlank(message = "用户性别不能为空")
   private String sex;

   /** 归属部门 */
   @NotNull(message = "归属部门不能为空")
   private List<Integer> deptIdList;

}
```

3. 接口方法出参类注释示例：

```java
/**
 * 用户实体
 **/
@Data
public class UserVo {

   /** 用户名 */
   private String username;

   /** 年龄 */
   private Integer age;

   /** 性别 */
   private String sex;

   /** 归属部门 */
   private List<Dept> deptList;

}
```

## 4 答疑
### 4.1 启动有问题怎么办？
在日志目录`./data/log`查看看一下日志报错，相信你看得懂滴。如果有需要也可以@我！

### 4.2 我不需要文档，只想要得到接口数据？
- 方式一：在这个服务页面中生成文档后通过页面接口获取json再进行解析； 
- 方式二：在你的项目中引用项目的核心解析包`lime-japi-docs-parser`（包有上传到maven中央仓库，直接引用就好），然后用这个包提供的工具解析，项目已开源至：https://gitee.com/xuchenoak/lime-japi-docs-parser （跳转过去看具体的使用说明，也多多支持这个包，蟹蟹）。

### 4.3 交流群
- 建了一个wx交流群为大家答疑，版本更新也会发到群里，争取让大家能第一时间体验新功能；
- 新功能都是我在工作中实际需求需要的，不一定适用于大家，所以大家如果有新的需求想法，也欢迎在群里一起探讨；
- wx群只有一周的有效期，码就不贴在这里了，可以先加我的wx（wx号：xuchenoak）拉进去，或提个issue后我回复群码，蟹蟹。

## 5 更新记录

- 2023-03-26 V1.0.1发布（这是一个小粗糙版本）
   - 启动一个服务仅支持解析一个项目生成单文档；
   - 文档配置需要在`application.yml`中进行配置；
   - 功能和页面都比较单一。


- 2024-06-06 V1.1.1更新（不兼容上一版本，需单独运行）
   - 启动一个服务可以配置多个项目生成多份文档；
   - 系统和文档配置都放在了页面上，简化为一个jar包，直接启动即可；
   - 支持从初始化开始可自定义系统名称、logo、slogan等；
   - 支持配置文档字段校验和默认值注释回调；
   - 修复了核心解析包的几个小bug。


- 2024-09-28 V1.1.2更新（兼容上一版本，更换jar包即可）
  - 文档搜索支持全局搜索，可以根据目录名称、接口名称或接口地址搜索并点击跳转接口所在位置；
  - 文档配置新增“文档标识码”字段，用于触发生成文档时作为入参，实现调用一次触发接口即可以触发多个相同“文档标识码”的文档生成；
  - 文档访问地址支持“文档/目录/接口”三级定位，访问带有相应参数的url即可定位到对应的文档、接口或目录。

## 6 最后&致谢

- 本项目的灵感源于`@YeDaxia`的项目 <a href="https://github.com/YeDaxia/JApiDocs" target="_blank">JApiDocs</a>，它是一个可以解析Java源码并生成接口文档（支持生成html静态页或markdown等）的工具；


- 本项目解析Java源码使用的是项目 <a href="http://javaparser.org/" target="_blank">javaparser</a> 提供的解析器；


- 本项目使用的前端框架是`vue`，UI框架是`ant-design-vue`，代码编辑器是`vue2-ace-editor`；


- 特别感谢小伙伴 <a href="https://weibo.com/u/7805144171" target="_blank">@hantai喔</a> 提供的logo设计，欢迎大家多多关注TA。
