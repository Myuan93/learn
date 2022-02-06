# mybatis和HIbernate
#### Q0 什么是ORM框架
ORM:全称是Object Relational Mapping(对象关系映射),其主要作用是在编程中,把面向对象的概念和数据库中表的概念对应起来
对应关系一般采用XML格式,好处是不用操作数据库表,直接操作对象即可.
### HIbernate
#### Q0:什么是HIbernate
HIbernate是一个基于jdbc的主流持久化框架,是一个优秀的ORM实现,简化了dao层编码工作.
#### Q1:HIbernate数据的三种状态
- 瞬时态:刚new出来的数据–内存有，数据库没有） 不和数据库的数据有任何关联关系，在Hibernate中，可通过session的save()或 saveOrUpdate()方法将瞬时对象与数据库相关联，并将数据对应的插入数据库中，此时该瞬时对象转变成持久化对象.
- 持久态:从数据查询的，或者刚保存到数据库，session没关闭的， 数据库有，内存也有） 处于该状态的对象在数据库中具有对应的记录，并拥有一个持久化标识。如果是用hibernate的delete()方法，对应的持久对象就变成瞬时对象，因数据库中的对应数据已被删除，该对象不再与数据库的记录关联。
- 游离态:(数据库有，内存没有） 当与某持久对象关联的session被关闭后，该持久对象转变为脱管对象。当脱管对象被重新关联到session上时，并再次转变成持久对象。
  在事务提交的时候，Hibernate去对比处于持久状态的数据是否发生改变，(快照区、一级缓存区)，当我们会话结束前，对持久状态数据进行了修改的话，快照区的数据会跟着改变。当session提交事务的时候，如果发现快照区和一级缓存的数据不一致，就会发送SQL进行修改。
#### Q2:Hibernate运行过程和原理
配置Configuration 、产生SessionFactory、创建session对象、启动事务、完成CURD、提交事务、关闭session。
所以核心是：Configuration、SessionFactory、Session
1. hibernate启动的时候利用Configuration读取xml配置文件
2. 通过配置文件创建SessionFactory对象，初始化hibernate基本信息
3. 获取session然后调用CRUD方法进行数据操作，hibernate会把我们的数据进行三种状态的划分，然后根据状态进行管理我们的数据，对应的发送SQL进行数据操作
4. 关闭session，如果有事务的情况下，需要手动获取事务并开启，然后事务结束后提交事务。
5. 在提交事务的时候，去验证我们的快照里面的数据和缓存数据是否一致，如果不一致，发送SQL进行修改。
#### Q3:Hibernate的get方法和load方法的区别1、get和load都是利用主键策略查询数据，
1. get和load都是利用主键策略查询数据，
2. get默认不使用懒加载机制，load默认要使用懒加载机制，所谓的懒加载就是我们这个数据如果不使用，hibernate就不发送SQL到数据库查询数据。
3. 当查询数据库不存在的数据的时候，get方法返回null，load方法抛出空指针异常， 原因是因为，load方法采用的动态代理的方式实现的，我们使用load方法的时候，Hibernate会创建一个该实体的代理对象，该代理只保存了该对象的ID，当我们访问该实体对象其他属性，Hibernate就发送SQL查询数据封装到代理对象，然后在利用代理对象返回给我们实际的数据。
#### Q4:Hibernate的缓存机制
HIbernate缓存分为2级缓存

一级缓存又叫session缓存，又叫事务级缓存，生命周期从事务开始到事务结束，一级缓存是hibernate自带的，暴力使用，当我们一创建session就已有这个缓存了。数据库就会自动往缓存存放.

二级缓存是hibernate提供的一组开放的接口方式实现的，都是通过整合第三方的缓存框架来实现的，二级缓存又叫sessionFactory的缓存，可以跨session访问。常用的EHcache、OScache，这个需要一些配置。

当我们每次查询数据的时候，首先是到一级缓存查看是否存在该对象，如果有直接返回，如果没有就去二级缓存进行查看，如果有直接返回，如果没有在发送SQL到数据库查询数据， 当SQL发送查询回该数据的时候，hibernate会把该对象以主键为标记的形式存储到二级缓存和一级缓存，如果返回的是集合，会把集合打散然后以主键的形式存储到缓存。一级缓存和二级缓存只针对以ID查询的方式生效，get、load方法。
#### Q5:Hibernate有哪几种查询数据的方式
有四种方式
- hql
- 条件查询QBC(Query By Criteria)
- QBE(query by Example) 通过Example编程接口进行查询
- 原生sql （通过createSQLQuery建立）
### mybatis
#### Q0:什么是mybatis
Mybatis内部封装了jdbc，开发者只需要关注sql语句本身，而不需要花费精力去处理加载驱动、创建连接、创建statement等繁杂的过程。
mybatis通过xml或注解的方式将要执行的各种statement配置起来，并通过java对象和statement中sql的动态参数进行映射生成最终执行的sql语句，最后由mybatis框架执行sql并将结果映射为java对象并返回。
#### Q1:mybatis工作原理
1. 读取Mybatis**配置文件mybatis-config.xml**。mybatis-config.xml作为Mybatis的全局配置文件，配置Mybatis的运行环境等信息，其中主要内容是获取数据库连接。
2. **加载映射文件Mapper.xml**。Mapper.xml文件即SQL映射文件，该文件中配置了操作数据库的SQL语句，需要在mybatis-config.xml中加载才能执行。mybatis-config.xml可以加载多个配置文件，每个配置文件对应数据库中的一张表。
3. **构建会话工厂**。通过Mybatis的环境等配置信息构建会话工厂SqlSessionFactory。
4. **创建SqlSession对象**。由会话工厂创建SqlSession对象，该对象中包含执行SQL的所有方法。
5. **Mybatis底层定义了一个Executor接口来操作数据库**，它会根据SqlSession传递的参数动态地生成需要执行的SQL语句，同时负责查询缓存的维护。
6. **在Executor接口的执行方法中，包含一个MappedStatement类型的参数，该参数对映射信息的封装，用于存储要映射的SQL语句的id、参数等**。Mapper.xml文件中一个SQL对应一个MappedStatement对象，SQL的id即是MappedStatement的id。
7. **输入参数映射**。在执行方法时，MappedStatement对象会对用户执行SQL语句的输入参数进行定义（可以定义为Map、List类型、基本类型和POJO类型），Executor执行器会通过MappedStatement对象在执行SQL前，将输入的Java对象映射到SQL语句中。这里对输入参数的映射过程就类似于JDBC编程中对preparedStatement对象设置参数的过程。
8. **输入结果映射**。在数据库中执行完SQL语句后，MappedStatement对象会对SQL执行输出的结果进行定义（可以定义为Map和List类型、基本类型、POJO类型），Executor执行器会通过MappedStatement对象在执行SQL语句后，将输出结果映射至Java对象中。这种将输出结果映射到Java对象的过程就类似于JDBC编程中对结果的解析处理过程。
#### Q2:mybatis缓存机制
Mybatis中有一级缓存和二级缓存，默认情况下一级缓存是开启的，而且是不能关闭的。

一级缓存是指 SqlSession 级别的缓存，当在同一个 SqlSession 中进行相同的 SQL 语句查询时，第二次以后的查询不会从数据库查询，而是直接从缓存中获取，一级缓存最多缓存 1024 条 SQL。
同一个 sqlsession 再次发出相同的 sql，就从缓存中取出数据。如果两次中间出现 commit 操作 （修改、添加、删除），本 sqlsession 中的一级缓存区域全部清空，下次再去缓存中查询不到所 以要从数据库查询，从数据库查询到再写入缓存。

二级缓存是指可以跨 SqlSession 的缓存。是 mapper 级别的缓存，对于mapper 级别的缓存不同的sqlsession 是可以共享的。

二级缓存的范围是 mapper 级别（mapper同一个命名空间，Namespace），mapper 以命名空间为单位创建缓存数据结构，结构是 map。mybatis 的二级缓存是通过 CacheExecutor 实现的。CacheExecutor其实是 Executor 的代理对象。所有的查询操作，在 CacheExecutor 中都会先匹配缓存中是否存在，不存在则查询数据库。
#### Q3: #{}和${}的区别是什么
`#{}`是预编译处理，`${}`是字符串替换。

Mybatis在处理#{}时，会将sql中的#{}替换为?号，调用PreparedStatement的set方法来赋值；

Mybatis在处理${}时，就是把${}替换成变量的值。

使用#{}可以有效的防止SQL注入，提高系统安全性。

预编译语句的优势在于归纳 为：一次编译、多次运行，省去了解析优化等过程；此外预编译语句能防止sql注入。
#### Q4:使用MyBatis的mapper接口调用时有哪些要求？
1. Mapper 接口方法名和 mapper.xml 中定义的每个 sql 的 id 相同
2. Mapper 接口方法的输入参数类型和 mapper.xml 中定义的每个 sql 的 parameterType 的类型相同
3. Mapper 接口方法的输出参数类型和 mapper.xml 中定义的每个 sql 的 resultType 的类型相同
4. Mapper.xml 文件中的 namespace 即是 mapper 接口的类路径
#### Q5:什么是MyBatis的接口绑定？有哪些实现方式？
接口绑定，就是在MyBatis中任意定义接口，然后把接口里面的方法和SQL语句绑定，我们直接调用接口方法就可以，这样比起原来了SqlSession提供的方法我们可以有更加灵活的选择和设置。

接口绑定有两种实现方式：

通过注解绑定，就是在接口的方法上面加上 @Select、@Update等注解，里面包含Sql语句来绑定；

通过xml里面写SQL来绑定， 在这种情况下，要指定xml映射文件里面的namespace必须为接口的全路径名。当Sql语句比较简单时候，用注解绑定， 当SQL语句比较复杂时候，用xml绑定，一般用xml绑定的比较多。
#### Q1 Hibernate与mybatis的对比
1. 开发速度对比
   如果项目中复杂查询少,大部分都是基本的增删改查,选择Hibernate更高效;如果复杂语句多,使用MyBatis就会加快很多，而且SQL语句管理也会很方便。
1. 开发工作量对比
   MyBatis与Hibernate都有相应的代码生成工具，可以生成简单的DAO层方法。针对高级查询，MyBatis需要手动编写SQL语句及resultMap。而Hibernate有良好的映射机制，开发者无需关心SQL的生成与结果映射，可以更关注于业务流程。
1. SQL优化方面
   Hibernate的查询会将表中的所有字段查询出来，这一点会消耗性能。Hibernate也可以自己手写SQL指定要查询的字段，但这样就会破坏了Hibernate开发的简洁性，一般不推荐。而MyBatis的SQL语句是自己手写的，我们可以根据需求指定要查询的字段。
   Hibernate的HQL语句调优需要将SQL打印出来，MyBatis的SQL是自己手动写的所以调整方便。但Hibernate具有自己的日志统计。Mybatis本身不带日志统计，使用Log4j进行日志记录。
1. 对象管理方面(mybatis半自动化)
   Hibernate 是完整的对象/关系映射解决方案，它提供了对象状态管理（state management）的功能，使开发者不再需要理会底层数据库系统的细节。也就是说，相对于常见的 JDBC/SQL 持久层方案中需要管理 SQL 语句，Hibernate采用了更自然的面向对象的视角来持久化 Java 应用中的数据。
   换句话说，使用 Hibernate 的开发者应该总是关注对象的状态（state），不必考虑 SQL 语句的执行。这部分细节已经由 Hibernate 掌管妥当，只有开发者在进行系统性能调优的时候才需要进行了解。而MyBatis在这一块没有文档说明，用户需要对对象自己进行详细的管理。
   Mybatis优势
- MyBatis可以进行更为细致的SQL优化，可以减少查询字段。
- MyBatis容易掌握，而Hibernate门槛较高。
  
  **Hibernate优势**
- Hibernate的DAO层开发比MyBatis简单，Mybatis需要维护SQL和结果映射。
- Hibernate对对象的维护和缓存要比MyBatis好，对增删改查的对象的维护要方便。
- Hibernate功能强大，数据库无关性好，O/R映射能力强。Hibernate数据库移植性很好，MyBatis的数据库移植性不好，不同的数据库需要写不同SQL。
- Hibernate有更好的二级缓存机制，可以使用第三方缓存。MyBatis本身提供的缓存机制不佳。


