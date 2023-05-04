# Base
Java基础架构，在SpringBoot的基础上，封装了Base模块，提取出基础的增、删、改、查。
对于基础的CRUD可以使用Base模块的CRUD，如果需要定制，可以重写Base模块的方法

# 说明
Spring的三层架构的Test开头文件都是测试文件，用于展示测试效果，可以删除。

一下四个文件都是基础文件，上面三个为Spring三层架构，BaseEntity是基础实体类，后面添加的三层架构和JavaBean都需要重写这四个类，
base/BaseController
base/BaseService
base/BaseMapper
base/BaseEntity
