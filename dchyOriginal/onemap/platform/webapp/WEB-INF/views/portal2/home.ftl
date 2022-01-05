<@base.main nav="home">
<div class="banner">
    <div class="banner-inner">
        <ul class="nav slider clearfix">
            <li style="background: #dcdcdc url(static/img/banner/banner-1.png) center center no-repeat;">
                <div class="container">
                    <div class="banner-desc">
                        <img src="resources/img/${env.getEnv('local.path')}/banner/desc-1p8.png"/>
                    </div>
                </div>
            </li>
            <li style="background: #04234D url(static/img/banner/banner-2.png) center center no-repeat;">
                <a href="<@com.rootPath/>/map/${env.getMainTpl()!'YZT_DEFAULT'}" class="container">
                    <div class="banner-desc" style="right: 0;">
                        <img src="resources/img/${env.getEnv('local.path')}/banner/desc-2p8.png"/>
                    </div>
                </a>
            </li>
            <#if env.getEnv('main.level')='provincial'>
            <li style="background: #4894D6 url(static/img/banner/banner-3.png) center center no-repeat;">
                <a href="portal2/rescenter/db" class="container">
                    <div class="banner-desc">
                        <img src="static/img/banner/desc-3p8.png"/>
                    </div>
                </a>
            </li></#if>
        </ul>
        <div class="toggles">
            <a href="#" class="open">open</a>
            <a href="#" class="cloze">cloze</a>
            <#if env.getEnv('main.level')='provincial'>
                <a href="#" class="cloze">cloze</a>
            </#if>
        </div>

    </div>
</div>
<!---<a>${env.getEnv("main.level")!}</a>-->
<div class="main">
    <div class="container">
    	<br />
        <div class="row" <#if env.getEnv('main.level')!='provincial'>style="margin-top: 40px;margin-bottom: 60px;"</#if>>
            <div class="span6">
                <div class="feature-wrap">
                    <a href="portal2/onemap" class="feature-inner">
                        <span class="ico-f ico-computer"></span>
                        <div class="desc large">
                            <h4>一张图</h4>
                            <p>以地图服务的形式，全面展现${env.getEnv('local.title')!}国土资源 “一张图”数据库中的空间数据。</p>
                        </div>
                    </a>
                </div>
            </div>
            <div class="span6">
                <div class="feature-wrap">
                    <a href="portal2/tpl/index" class="feature-inner">
                        <span class="ico-f ico-docs"></span>

                        <div class="desc large">
                            <h4>专题图</h4>

                            <p>按基础地理、遥感影像、土地利用现状、土地利用总体规划、矿产资源规划等业务专题，分类浏览“一张图”数据库中的空间数据。</p>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    <#if env.getEnv('main.level')='provincial'>
        <div class="row">
            <div class="span4">
                <div class="feature-wrap">
                    <a href="portal2/statistic" class="feature-inner">
                        <span class="ico-f ico-analysis"></span>

                        <div class="desc small">
                            <h4>统计图表</h4>

                            <p>提供数据统计分析功能，并以图表的形式进行展现。</p>
                        </div>
                    </a>
                </div>
            </div>
            <div class="span4">
                <div class="feature-wrap">
                    <a href="portal2/rescenter/db" class="feature-inner">
                        <span class="ico-f ico-city"></span>

                        <div class="desc small">
                            <h4>资源中心</h4>

                            <p>提供针对“一张图”数据库、地图服务等各类资源的查询检索。</p>
                        </div>
                    </a>
                </div>
            </div>
            <div class="span4">
                <div class="feature-wrap">
                    <a href="portal2/api/index" class="feature-inner">
                        <span class="ico-f ico-map"></span>

                        <div class="desc small">
                            <h4>地图API</h4>

                            <p>提供基于“一张图”数据库的地图服务二次开发接口及帮助文档。</p>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </#if>
    </div>
</div>
</@base.main>