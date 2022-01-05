<template>
    <div class="no-access">
        <div class="no-access-content">
            <div>
                <img src="static/images/access.png" alt="">
            </div>
            <div class="margin-top-20">
                <span class="no-access-tip">没有访问权限</span>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    
}
</script>
<style scoped>
    .no-access {
        height: calc(100vh - 20px);
        background-color: #fff;
        margin: 10px;
        position: relative;
    }
    .no-access-content {
        width: 200px;
        text-align: center;
        position: absolute;
        top: 40%;
        left: 50%;
        transform: translateX(-100px);
    }
    .no-access-tip {
        font-size: 18px;
    }
</style>