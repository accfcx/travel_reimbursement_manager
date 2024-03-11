package com.test.recipe.controller;

import com.test.recipe.common.ResponseResult;
import com.test.recipe.dto.BpmRequest;
import com.test.recipe.mapper.ProcessDefMapper;
import com.test.recipe.model.ProcessDef;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // 配置审批模版元信息
    @GetMapping("/modelMetaList")
    public List<ProcessDef> modelMetaList() {
        List<ProcessDef> all = processDefMapper.findAll();
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
        pmsModel.setCreateTime(new Date());
        pmsModel.setUpdateTime(new Date());
        processDefMapper.insert(pmsModel);

        return new ResponseResult();
    }

    @PostMapping("/updateModelMeta")
    public void update(@RequestBody BpmRequest bpmRequest) {
        // 添加流程xml定义
        ProcessDef pmsModel = new ProcessDef();
        pmsModel.setStatus(bpmRequest.getStatus());
        pmsModel.setUpdateTime(new Date());
        processDefMapper.update(pmsModel);
    }

    @PostMapping("/deployXml")
    public void savePmsModel(@RequestBody BpmRequest bpmRequest) {
        ProcessDef processDef = processDefMapper.findById(bpmRequest.getId());
        String xmlStr = processDef.getProcessXml();

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addString(processDef.getProcessKey() + ".bpmn20.xml", xmlStr).name(processDef.getProcessName());
        Deployment deploy = deploymentBuilder.deploy();

        String deploymentId = deploy.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();

        String processDefinitionId = processDefinition.getId();
        String processDefinitionKey = processDefinition.getKey();

        processDef.setStatus(1);
        processDef.setUpdateTime(new Date());

        processDef.setUpdateUid(bpmRequest.getUpdateUid());
        processDef.setProcessDefinitionKey(processDefinitionKey);
        processDefMapper.update(processDef);
        log.info("部署={}, processDefinitionId={},processDefinitionKey={}  ", deploy, processDefinitionId, processDefinitionKey);
    }

    // 创建审批实例
    @PostMapping("/startProcess")
    public Object startProcess(@RequestParam String processDefinitionKey, @RequestParam String businessKey, @RequestBody Map<String, Object> variables) {
        ExecutionEntityImpl start = (ExecutionEntityImpl) runtimeService.createProcessInstanceBuilder().processDefinitionKey(processDefinitionKey).variables(variables).businessKey(businessKey).start();
        log.info("new getBusinessKey:{}", start.getBusinessKey());
        log.info("new getProcessDefinitionId:{}", start.getProcessDefinitionId());
        log.info("new getProcessInstanceId:{}", start.getProcessInstanceId());
        log.info("new id:{}", start.getId());
        log.info("new getDeploymentId:{}", start.getDeploymentId());
        return start;
    }
}







