<template>
    <div style="width: 100%">
        <a class="process-chart" href="javascript: void(0)" @click="showProcessChart()">流程图</a>
        <Modal
            class="modal-base"
            v-model="showProcess"
            :title="'流程图'"
            width="800"
            :mask-closable="false"
            :footer-hide="true"
            closable
        >
            <div class="step-process">
                div
            </div>
        </Modal>
    </div>
</template>
<script>
export default {
    name: "processChart",
    data() {
        return {
            showProcess: false,
            btns: [
                {
                    text: '确认',
                    class: 'main-btn-a',
                    action: () => {
                        this.cancel()
                    }
                }
            ]
        }
    },
    methods: {
        showProcessChart(){
            console.log(1)
            this.showProcess = true
        },
        cancel(){
            this.showProcess = false
        }
    },
}
</script>
<style scoped>
    .process-chart {
        color: #333;
    }
    .step-process {
        width: 400px;
        margin: 20px auto 0;
        height: 400px;
    }
</style>