<template>
    <div>
        <table class="pay-info-table">
            <tr>
                <td>支付金额</td>
                <td>{{payInfoData.zfje || ""}}</td>
            </tr>
            <tr>
                <td>支付时间</td>
                <td>{{payInfoData.zfsj || ""}}</td>
            </tr>
            <tr>
                <td>支付方式</td>
                <td>{{payInfoData.zffs || ""}}</td>
            </tr>
        </table>
    </div>
</template>
<script>
export default {
    props: {
        payInfoData: {
            type: Object,
            default: () => {
                return {}
            }
        }
    }
}
</script>
<style scoped>
    .pay-info-table{
        width: 60%;
    }
    .pay-info-table tr {
        height: 44px;
        line-height: 44px;
        width: 100%;
        border: 1px solid rgb(240, 239, 239);;
    }
    .pay-info-table tr td:nth-child(2n-1){
        width: 30%;
        text-align: center;
        background-color: rgb(240, 239, 239);
    }
    .pay-info-table tr td:nth-child(2n){
        width: 70%;
        text-align: center;
        font-weight: bold;
    }
</style>