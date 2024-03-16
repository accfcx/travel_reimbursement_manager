package com.test.recipe.controller;

import com.google.gson.Gson;
import com.test.recipe.common.ResponseResult;
import com.test.recipe.dto.BpmRequest;
import com.test.recipe.dto.VariableDto;
import com.test.recipe.enums.RecipeType;
import com.test.recipe.mapper.PersonMapper;
import com.test.recipe.mapper.ProcessDefMapper;
import com.test.recipe.model.Person;
import com.test.recipe.model.ProcessDef;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.StringReader;

import org.xml.sax.InputSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author accfcx
 **/
@RestController
@RequestMapping("/process")
@Slf4j
public class ProcessController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessDefMapper processDefMapper;

    @Autowired
    private HistoryService historyService;

    @Resource
    PersonMapper personMapper;

    @Resource
    Gson gson;

    // 配置审批模版元信息
    @GetMapping("/modelMetaList")
    public List<ProcessDef> modelMetaList() {
        List<ProcessDef> all = processDefMapper.findAll();

        all.forEach(item -> {
            Long submitUid = item.getSubmitUid();
            if (Objects.nonNull(submitUid) && submitUid != 0l) {
                Person submit = personMapper.findById(submitUid);
                item.setSubmitName(submit.getNameZh());
            }
            Long updateUid = item.getUpdateUid();
            if (Objects.nonNull(updateUid) && updateUid != 0l) {
                Person update = personMapper.findById(updateUid);
                item.setUpdateName(update.getNameZh());
            }

            item.setRecipeType(RecipeType.getDescByCode(item.getRecipeType()));
        });
        return all;
    }

    @PostMapping("/addModelMeta")
    public ResponseResult add(@RequestBody BpmRequest bpmRequest) {

        ProcessDef def = processDefMapper.findByType(bpmRequest.getRecipeType());
        if (Objects.nonNull(def)) {
            ResponseResult<String> result = new ResponseResult<>();
            result.setCode(100);
            result.setMsg("业务场景已关联到存在的流程:" + def.getProcessName());

            return result;
        }

        // 添加基础定义
        ProcessDef pmsModel = new ProcessDef();
        pmsModel.setStatus(bpmRequest.getStatus());
        pmsModel.setProcessName(bpmRequest.getProcessName());
        pmsModel.setProcessKey(bpmRequest.getProcessKey());
        pmsModel.setRecipeType(bpmRequest.getRecipeType());
        pmsModel.setSubmitUid(bpmRequest.getSubmitUid());
        pmsModel.setUpdateUid(bpmRequest.getSubmitUid());
        pmsModel.setCreateTime(new Date());
        pmsModel.setUpdateTime(new Date());
        processDefMapper.insert(pmsModel);

        return new ResponseResult();
    }

    @PostMapping("/saveModelMeta")
    public void update(@RequestBody BpmRequest bpmRequest) {

        long id = bpmRequest.getId();
        Long updateUid = bpmRequest.getUpdateUid();
        updateUid = 3l;

        ProcessDef def = processDefMapper.findById(id);

        // 识别变量
        List<VariableDto> variableDtoList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(def.getProcessXml()));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("userTask");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String assignee = eElement.getAttribute("activiti:assignee");
                    // Remove the ${ and } to get the variable name
                    if (assignee.startsWith("${")) {
                        String variableName = assignee.replace("${", "").replace("}", "");

                        VariableDto variableDto = new VariableDto();
                        variableDto.setTaskId(eElement.getAttribute("id"));
                        variableDto.setVariable(variableName);

                        variableDtoList.add(variableDto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 更新流程xml定义
        def.setProcessXml(bpmRequest.getProcessXml());
        def.setUpdateUid(updateUid);
        def.setUpdateTime(new Date());
        if (!variableDtoList.isEmpty()) {
            def.setVariables(gson.toJson(variableDtoList));
        }

        processDefMapper.update(def);
    }

    @PostMapping("/deployXml")
    public void savePmsModel(@RequestBody BpmRequest bpmRequest) {
        ProcessDef processDef = processDefMapper.findById(bpmRequest.getId());
        String xmlStr = processDef.getProcessXml();

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addString(processDef.getProcessKey() + ".bpmn20.xml", xmlStr).name(processDef.getProcessName());
        Deployment deploy = deploymentBuilder.deploy();

        String deploymentId = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        String processDefinitionId = processDefinition.getId();
        String processDefinitionKey = processDefinition.getKey();

        log.info("部署={}, processDefinitionId={},processDefinitionKey={}  ", deploy, processDefinitionId, processDefinitionKey);

        processDef.setStatus(1);
        processDef.setUpdateTime(new Date());

        processDef.setUpdateUid(1l);
        processDef.setProcessDefinitionKey(processDefinitionKey);
        processDef.setProcessDefinitionId(processDefinitionId);
        processDef.setPublishProcessXml(xmlStr);

        if (StringUtils.isNotEmpty(processDef.getVariables())) {
            processDef.setPublishVariables(processDef.getVariables());
        }

        processDefMapper.update(processDef);
    }

    @GetMapping("/test/{id}")
    public void test(@PathVariable long id) {
        ProcessDef def = processDefMapper.findById(id);

        List<VariableDto> variableDtoList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(def.getProcessXml()));
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("userTask");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String assignee = eElement.getAttribute("activiti:assignee");
                    // Remove the ${ and } to get the variable name
                    if (assignee.startsWith("${")) {
                        String variableName = assignee.replace("${", "").replace("}", "");

                        VariableDto variableDto = new VariableDto();
                        variableDto.setTaskId(eElement.getAttribute("id"));
                        variableDto.setVariable(variableName);

                        variableDtoList.add(variableDto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(gson.toJson(variableDtoList));
    }
}







