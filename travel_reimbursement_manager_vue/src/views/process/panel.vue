<template>
  <div class="containers" ref="content">
    <div class="canvas" ref="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>
    <div>
      <ul class="buttons">
        <li v-if="isEditMode">
          <a ref="saveSvg2" href="javascript:" @click="saveXmlToDb">保存</a>
        </li>
        <li v-if="isEditMode">
          <a ref="saveDiagram" href="javascript:">导出BPMN</a>
        </li>
        <li v-if="isEditMode">
          <a ref="saveSvg" href="javascript:">导出SVG</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import BpmnModeler from 'bpmn-js/lib/Modeler'
import propertiesPanelModule from 'bpmn-js-properties-panel'
import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda'
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda'
import {xmlStr} from '../mock/xmlStr'

export default {
  name: '',
  components: {},
  created() {
  },
  mounted() {
    let rowData = this.$route.query.rowData;
    this.curXmlStr = rowData.modelXml
    this.id = rowData.id
    this.modelName = rowData.modelName
    this.modelKey = rowData.modelKey
    this.init()
  },
  data() {
    return {
      bpmnModeler: null,
      container: null,
      canvas: null,
      curXmlStr: null,
      id: null,
      modelKey: null,
      modelName: null,
      loggedName: sessionStorage.getItem("loggedName"),
      xmlSaveRequest: {
        xmlStr: null,
        modelName: null,
        modelKey: null,
        id: null,
        user: null
      }
    }
  },
  methods: {
    init() {
      this.container = this.$refs.content
      const canvas = this.$refs.canvas
      this.bpmnModeler = new BpmnModeler({
        container: canvas,
        propertiesPanel: {
          parent: '#js-properties-panel'
        },
        additionalModules: [
          propertiesProviderModule,
          propertiesPanelModule
        ],
        moddleExtensions: {
          camunda: camundaModdleDescriptor
        }
      })
      this.createNewDiagram()
    },
    createNewDiagram() {
      let xml = xmlStr;
      if (this.curXmlStr != null && this.curXmlStr !== '') {
        xml = this.curXmlStr;
      }
      var jsonObj = this.$x2js.xml2js(xml)
      this.returnFormProperty(jsonObj)
      let newXml = this.$x2js.js2xml(jsonObj)
      newXml = newXml.replaceAll('activiti:assignee', 'camunda:assignee')

      this.bpmnModeler.importXML(newXml, (err) => {
        if (err) {
          console.log("导入xml异常:" + err)
        } else {
          this.success()
        }
        var canvas = this.bpmnModeler.get('canvas')
        canvas.zoom('fit-viewport')
      })
    },
    success() {
      this.addEventBusListener()
      this.addBpmnListener()
      this.addModelerListener()
    },
    addEventBusListener() {
      let that = this
      const eventBus = this.bpmnModeler.get('eventBus')
      const eventTypes = ['element.click', 'element.changed', 'element.updateLabel']
      eventTypes.forEach(function (eventType) {
        eventBus.on(eventType, function (e) {
          if (!e || e.element.type == 'bpmn:Process') return
          if (eventType === 'element.changed') {
            // that.elementChanged(e)
          } else if (eventType === 'element.click') {
            // if (e.element.type === 'bpmn:Task') {
            // }
          } else if (eventType === 'element.updateLabel') {
          }
        })
      })
    },
    addBpmnListener() {
      const that = this
      const downloadLink = this.$refs.saveDiagram
      const downloadSvgLink = this.$refs.saveSvg
      const saveSvg2 = this.$refs.saveSvg2
      this.bpmnModeler.on('commandStack.changed', function () {
        that.saveSVG(function (err, svg) {
          that.setEncoded(downloadSvgLink, 'diagram.svg', err ? null : svg)
        })
        that.saveDiagram(function (err, xml) {
          that.setEncoded(downloadLink, 'diagram.bpmn', err ? null : xml)
        })
        that.updateXMl((err, xml) => {
          saveSvg2.className = 'active'
          that.curXmlStr = xml
        })
      })
    },
    addModelerListener() {
      const bpmnjs = this.bpmnModeler
      const that = this
      // 'shape.removed'
      const events = ['shape.added', 'shape.move.end', 'connect.end', 'connection.create', 'connection.move']
      events.forEach(function (event) {
        that.bpmnModeler.on(event, e => {
          var elementRegistry = bpmnjs.get('elementRegistry')
          var shape = e.element ? elementRegistry.get(e.element.id) : e.shape
        })
      })
    },
    saveSVG(done) {
      this.bpmnModeler.saveSVG(done)
    },
    saveDiagram(done) {
      this.bpmnModeler.saveXML({format: true}, function (err, xml) {
        done(err, xml)
      })
    },
    updateXMl(done) {
      this.bpmnModeler.saveXML({format: true}, function (err, xml) {
        done(err, xml)
      })
    },
    setEncoded(link, name, data) {
      // 把xml转换为URI，下载要用到的
      const encodedData = encodeURIComponent(data)
      let xmlFile = new File([data], 'test.bpmn')
      if (data) {
        link.className = 'active'
        link.href = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData
        link.download = name
      }
    },
    saveXmlToDb() {
      const xml = this.curXmlStr;
      const jsonObj = this.$x2js.xml2js(xml);
      this.getFormProperty(jsonObj);
      let newXml = this.$x2js.js2xml(jsonObj);

      this.xmlSaveRequest.xmlStr = newXml
      this.xmlSaveRequest.id = this.id
      this.xmlSaveRequest.modelName = this.modelName
      this.xmlSaveRequest.modelKey = this.modelKey
      this.xmlSaveRequest.user = this.loggedName
      this.$http.post("/process/saveModelMeta", this.xmlSaveRequest).then(res => {
        this.$message.info("保存流程模版定义成功")
      }).catch(err => {
        this.$message.error("保存xml数据失败，请联系管理员~")
      })
    },
    getFormProperty(json) {
      for (let e in json) {
        if (e == 'extensionElements' && json.extensionElements.formData && json.extensionElements.formData.formField) {
          let formProperty = JSON.parse(JSON.stringify(json.extensionElements.formData.formField));
          if (this.isArrayFn(formProperty)) {
            formProperty.forEach(x => {
              x.__prefix = 'activiti';
              x._name = x._label;
              if (x._defaultValue) {
                x._default = x._defaultValue;
              }
            });
          } else {
            formProperty.__prefix = 'activiti';
            formProperty._name = formProperty._label;
            if (formProperty._defaultValue) {
              formProperty._default = formProperty._defaultValue;
            }
          }
          json.extensionElements.formProperty = formProperty;
          delete json.extensionElements.formData;
        } else if (e.includes('camunda:')) {
          let str = e.replace('camunda:', 'activiti:');
          json[str] = json[e];
          delete json[e];
        } else if (typeof json[e] == 'object') {
          this.getFormProperty(json[e]);
        }
      }
    },
    returnFormProperty(json) {
      for (let e in json) {
        if (e == 'extensionElements' && json.extensionElements.formProperty) {
          let formField = JSON.parse(JSON.stringify(json.extensionElements.formProperty))
          if (this.isArrayFn(formField)) {
            formField.forEach(x => {
              x.__prefix = 'camunda'
            })
          } else {
            formField.__prefix = 'camunda'
          }
          json.extensionElements.formData = {
            formField,
            _businessKey: json.extensionElements._businessKey,
            __prefix: "camunda"
          }
          delete json.extensionElements.formProperty
        } else if (e.startsWith('activiti:')) {
          let camundaKey = e.replace('activiti:', 'camunda:');
          json[camundaKey] = json[e];
          delete json[e];
        } else if (typeof json[e] == 'object') {
          this.returnFormProperty(json[e])
        }
      }
    },
    isArrayFn(value) {
      if (typeof Array.isArray === "function") {
        return Array.isArray(value);
      } else {
        return Object.prototype.toString.call(value) === "[object Array]";
      }
    }
  },
  computed: {
    isEditMode() {
      return this.$route.query.mode === 'edit';
    }
  }
}
</script>

<style scoped>
.containers {
  background-color: #ffffff;
  width: 100%;
  height: calc(100vh - 52px);
}

.canvas {
  width: 100%;
  height: 100%;
}

.panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
}

.buttons {
  position: relative;
  left: 500px;
}

.buttons li {
  display: inline-block;
  margin: 5px;
}

.buttons li a {
  color: #999;
  background: #eee;
  cursor: not-allowed;
  padding: 8px;
  border: 1px solid #ccc;
  text-decoration: none;
}

.buttons li a.active {
  color: #333;
  background: #fff;
  cursor: pointer;
}
</style>