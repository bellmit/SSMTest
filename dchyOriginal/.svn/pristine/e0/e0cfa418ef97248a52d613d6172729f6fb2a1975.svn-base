<template>
    <div class="pdf">
        <div>
            <Button type="primary" class="btn-h-34 bdc-major-btn margin-left-10" @click="deliverData">确认交付</Button>
        </div>
        <iframe :src="urlSrc" style="width: 100%;min-height: calc(100vh - 6px)" type="application/pdf"/>
    </div>
</template>
<script>
import { deliverData } from "../../service/manage"
export default {
    data() {
        return {
            urlSrc: "",
            chxmid: ""
        }
    },
    mounted() {
        this.chxmid = this.$route.query.chxmid || "";
        this.urlSrc = 'static/pdf/web/viewer.html?file=' + encodeURIComponent(this.$route.query.url);
    },
    methods: {
        deliverData(){
            layer.confirm("确认交付?", (index) => {
                let params = {
                    chxmid: this.chxmid
                }
                layer.close(index)
                this.$loading.show("加载中...")
                deliverData(params).then(res => {
                    this.$loading.close();
                    layer.msg("交付成功")
                    window.close();
                }) 
            })
        },
        print(){
            this.$refs.printPdf.print()
        }
    }
}
</script>
<style scoped>
    .pdf {
        background-color: #666;
        position: relative;
    }
    .pdf-view {
        width: 50%;
        margin: 0 auto;
    }
    .bdc-major-btn {
        margin: 13px 0;
    }
    .bdc-major-btn:hover {
        background-color: #2d8cf0!important;
    }
</style>