Goods Management System Interface with RFID <br/> (基于RFID的物品管理系统界面)
=============================================

> Using Language(使用语言): Java/Swing    
> 仅仅是一个样例，仅供学习和参考，部分功能还未实现。    
> (Only a sample, for learning and reference, part of the function has not been achieved.)

Task Lists: <br/> (代办任务)
-----------
- [x] 登录界面搭建
- [x] 基于SQLite数据库操作的简单实现
- [ ] RFID的串口编程简单接口
- [ ] 界面与RFID串口的连接实现

Dependences (based on Debian 8 Jessie): <br/> (基于Debian系统下的依赖库)
------------
- OpenJDK 8:    
    `sudo apt-get install openjdk-8-jdk`
- SQLJet:    
    Official site: http://sqljet.com/    
    `sudo apt-get install libsqljet-java`
- RXTX Library:    
    Official site: http://rxtx.qbang.org/wiki/index.php/Main_Page    
    `sudo apt-get install librxtx-java`

> All apt libraries is under `/user/share/java/`.    
> (所有apt安装的包都在目录`/usr/share/java/`下)。    
> Linked library lists:    
> (链接的库有下列几项)
> - sqljet.jar    
> - antlr3-runtime.jar    
> - RXTXcomm.jar    
