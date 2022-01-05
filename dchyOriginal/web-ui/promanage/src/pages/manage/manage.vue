<template>
    <div class="sys-manage">
        <div class="content">
            <Tabs v-if="$route.name" value="name1">
                <TabPane :label="$route.name" name="name1"></TabPane>
            </Tabs>
            <router-view/>
        </div>
    </div>
</template>
<script>
export default {
    data() {
        return {
            
        }
    }
}
</script>
<style lang="less" scoped>
    .top-menu {
        width: 100%;
        height: 52px;
        line-height: 52px;
        border: 1px solid #ccc;
        padding-left: 20px;
    }
    .content {
        padding: 10px;
    }
    .content-title {
        color: #999;
        font-weight: bold;
        margin-bottom: 20px;
    }
    .sys-manage {
        min-height: calc(100vh - 20px);
        margin: 10px;
        background-color: #fff;
    }
</style>