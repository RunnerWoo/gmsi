基于RFID的物品管理系统界面
======================

> 使用语言: Java/Swing    
> 仅仅是一个样例，仅供学习和参考，部分功能还未实现。    

代办任务
-----------
- [x] 登录界面搭建
- [x] 基于SQLite数据库操作的简单实现
- [x] RFID的串口编程简单接口
- [x] 界面与RFID串口的连接实现

基于Debian系统下的依赖库
------------
- [x] OpenJDK 8:    
    `sudo apt-get install openjdk-8-jdk`
- [x] SQLJet:    
    Official site: http://sqljet.com/    
    `sudo apt-get install libsqljet-java`
- [ ] RXTX Library(选择其它库代替):    
    Official site: http://rxtx.qbang.org/wiki/index.php/Main_Page    
    `sudo apt-get install librxtx-java`
- [x] [Java Simple Serial Connector](https://github.com/scream3r/java-simple-serial-connector)

> 所有apt安装的包都在目录`/usr/share/java/`下    
> 链接的库有下列几项：    
> - sqljet.jar    
> - antlr3-runtime.jar    
> - jssc.jar    
