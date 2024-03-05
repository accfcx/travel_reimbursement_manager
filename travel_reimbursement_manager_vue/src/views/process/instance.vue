<template>
  <div ref="canvas"></div>
</template>

<script>
import BpmnJS from 'bpmn-js/dist/bpmn-navigated-viewer.production.min.js';


export default {
  data() {
    return {
      viewer: null,
      processInstanceId: null,
      processDefinitionKey: null,
      bpmnXml: null
    };
  },
  mounted() {
    this.viewer = new BpmnJS({container: this.$refs.canvas});
    this.processDefinitionKey = this.$route.query.rowData.processDefinitionKey
    this.processInstanceId = this.$route.query.rowData.processInstanceId
    this.loadDiagram();
  },
  methods: {
    async loadDiagram() {
      try {
        // 1. 获取 BPMN XML
        const xmlResponse = await this.$http.get(`http://localhost:8086/definition/${this.processDefinitionKey}`);
        const bpmnXML = xmlResponse.data;

        await new Promise((resolve, reject) => {
          this.viewer.importXML(bpmnXML, function (err) {
            if (err) {
              reject(err);
            } else {
              resolve();
            }
          });
        });

        // 3. 获取活动节点
        const activityResponse = await this.$http.get(`http://localhost:8086/instance/${this.processInstanceId}`);
        const activityIds = activityResponse.data.activeActivityIds;
        const finishedTaskList = activityResponse.data.finishedTaskList;

        // 4. 高亮显示活动节点
        const canvas = this.viewer.get('canvas');
        activityIds.forEach(id => {
          console.log("当前id:" + id)
          canvas.addMarker(id, 'highlight');
        });

        finishedTaskList.forEach(id => {
          console.log("当前id:" + id)
          canvas.addMarker(id, 'highlight2');
        });
      } catch (error) {
        console.error('Failed to load diagram', error);
      }
    }
  },
  beforeDestroy() {
    if (this.viewer) {
      this.viewer.destroy();
    }
  }
};
</script>

<style>
div {
  height: 600px; /* Set the height of the canvas */
}

.highlight .djs-visual > * {
  stroke: red !important;
}

.highlight2 .djs-visual > * {
  stroke: green !important;
}

.bjs-powered-by {
  display: none !important;
}


</style>
