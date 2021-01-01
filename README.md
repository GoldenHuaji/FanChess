# FanChess
这是一个游戏项目，欢迎 Pull & Star!

# Q & A
> Q: 我该如何运行它？
> 
> A:
> + Windows: 安装 JRE 环境，然后双击文件即可。
> + Linux: Linux 用户总有办法解决问题的 :)

> Q: 如何走棋？
> 
> A: 红方黑方交替走棋，第一个翻棋子的人是红方。吃子有大小顺序：帅 -> 士 -> 象 -> 车 -> 马 -> 兵。__特别地__，兵 -> 帅，炮 -> 全部（当炮和被吃的子之间有一个棋子时，炮可以吃掉这个棋子，无论该棋子是否翻开，任何**非兵**棋子都可以吃掉炮）

# 资源包
资源包是为了帮助用户提升使用体验，属于 .zip 压缩文件，以下为制作方法：
1. 一个资源包必须包含 config.json 才能被识别，config.json 应该包含以下内容：
 + author: 作者
 + desc: 描述
 + copyright: 版权
 示例：
 ```json
 {
	"author":"Goldebug",
	"desc":"开发测试",
	"备注":"JSON中可以不含部分KEY，比如本文件就没有 copyright。资源包中没有的图片将调用系统的图片。"
}
 ```
2. 图片大小要求所有棋子是 **128x** 大小，棋盘是**2:1** 大小
3. 当资源包中不包含某张图片时，会**调用原图片**
4. 您的资源包可以包含以下图片（必须是PNG格式）：<del>我不想打了，自己打开应用看</del>
5. 您的资源包**不应该**包含这个图片：空白.png

# 贡献者
感谢这些人对这个项目作出了贡献（排名不分先后）：
 + Acyons:图标素材
 + GoldenHuaji:代码编写
