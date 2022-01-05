<template>
    <div class="main-index">
        <router-view />
        <van-tabbar class="home-tab" v-model="active">
            <van-tabbar-item name="home" to="/home">办事大厅
                <template #icon>
                    <img v-if="active=='mine'" src="static/images/banshidating.png" alt="">
                    <img v-else src="static/images/banshidating-hover.png" alt="">
                </template>
            </van-tabbar-item>
            <van-tabbar-item name="mine" to="/mine">个人中心
                <template #icon>
                    <img v-if="active=='home'" src="static/images/gerenzhongxin.png" alt="">
                    <img v-else src="static/images/gerenzhognxin-hover.png" alt="">
                </template>
            </van-tabbar-item>
        </van-tabbar>
    </div>
</template>
<script>
export default {
    data() {
        return {
            active: "home"
        }
    },
    beforeRouteEnter (to, from, next) {
        if(to.path.startsWith("/mine")){
            next(vm => {
                vm.active = 'mine'
            })
        } else {
            next();
        }
    }
}
</script>
<style scoped>
    .main-index {
        padding-bottom: 95px;
    }
</style>