<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>HttpInvoker接口</h3>
    <h4>1.简介</h4>
    <p>&nbsp;&nbsp;&nbsp;Spring HTTP Invoker是spring框架中的一个远程调用模型，执行基于HTTP的远程调用，也就是说，可以通过防火墙，并使用java的序列化机制在网络间传递对象。客户端可以很轻松的像调用本地对象一样调用远程服务器上的对象，要注意的一点是，服务端、客户端都是使用Spring框架。为方便各种应用系统调用，omp系统中的查询，插入等地理操作接口也支持此种调用方式。</p>
    <h4>2. HTTP invoker 服务模式</h4>
    <table style="width: 100%;" border="1">
        <tr>
            <th>服务器端</th>
            <td>通过HTTP invoker服务将服务接口的某个实现类提供为远程服务</td>
        </tr>
        <tr>
            <th>客户端(应用系统)</th>
            <td>通过HTTP invoker代理向服务器端发送请求，远程调用服务接口的方法</td>
        </tr>
        <tr>
            <th>传输数据</th>
            <td>服务器端与客户端通信的数据需要序列化</td>
        </tr>
    </table>
    <h4>3. 配置说明</h4>
    <p>HTTP invoker配置主要分为服务器端和客户端配置，由于接口服务在omp系统中已经配置好且公开，下面着重说明在客户端（httpinvoker方式调用接口的应用系统）中的配置：</p>
    <p>(1)&nbsp;maven引用配置(此处默认应用系统是maven工程):<br/>
        需要引入spring框架: <br />
        &lt;dependency&gt;<br />
        &lt;groupId&gt;org.springframework&lt;/groupId&gt;<br />
        &lt;artifactId&gt;spring-web&lt;/artifactId&gt;<br />
        &lt;/dependency&gt;<br />
        &lt;dependency&gt;<br />
        &lt;groupId&gt;org.springframework&lt;/groupId&gt;<br />
        &lt;artifactId&gt;spring-webmvc&lt;/artifactId&gt;<br />
        &lt;/dependency&gt;<br />
        &lt;dependency&gt;<br />
        &lt;groupId&gt;org.springframework&lt;/groupId&gt;<br />
        &lt;artifactId&gt;spring-orm&lt;/artifactId&gt;<br />
        &lt;/dependency&gt;<br />
        &lt;dependency&gt;<br />
        &lt;groupId&gt;org.springframework&lt;/groupId&gt;<br />
        &lt;artifactId&gt;spring-context-support&lt;/artifactId&gt;<br />
        &lt;/dependency&gt;<br />
        &lt;dependency&gt;<br />
        &lt;groupId&gt;org.springframework&lt;/groupId&gt;<br />
        &lt;artifactId&gt;spring-tx&lt;/artifactId&gt;<br />
        &lt;/dependency&gt;<br />
        &lt;dependency&gt;<br />
        &lt;groupId&gt;org.springframework&lt;/groupId&gt;<br />
        &lt;artifactId&gt;spring-aop&lt;/artifactId&gt;<br />
        &lt;/dependency&gt;</p>
    <p>(2)&nbsp;spring配置：
    </p>
    <p>&lt;bean id=&quot;httpInvokerRequestExecutor&quot;<br />
        class=&quot;org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor&quot;&gt;<br />
        &lt;/bean&gt;</p>
    <p> &lt;bean id=&quot;geoService&quot; class=&quot;org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean&quot;&gt;<br />
        &lt;property name=&quot;httpInvokerRequestExecutor&quot; ref=&quot;httpInvokerRequestExecutor&quot;/&gt;<br />
        &lt;property name=&quot;serviceUrl&quot; value=&quot;<span>$</span>{omp.url}/geoService&quot;/&gt;<br />
        &lt;property name=&quot;serviceInterface&quot; value=&quot;cn.gtmap.useplan.service.GeoService&quot;/&gt;<br />
        &lt;/bean&gt;</p>
</div>