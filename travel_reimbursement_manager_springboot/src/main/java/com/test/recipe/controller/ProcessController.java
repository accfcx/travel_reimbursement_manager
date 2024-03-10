package com.test.recipe.controller;

import com.test.recipe.dto.BpmRequest;
import com.test.recipe.mapper.ProcessDefMapper;
import com.test.recipe.model.ProcessDef;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private ProcessDefMapper pmsModelRepository;

    @Autowired
    private HistoryService historyService;

    // 配置审批模版元信息
    @GetMapping("/modelMetaList")
    public List<ProcessDef> modelMetaList() {
        List<ProcessDef> all = pmsModelRepository.findAll();
        return all;
    }

    @PostMapping("/saveModelMeta")
    public void saveModelMeta(@RequestBody BpmRequest bpmRequest) {
        ProcessDef pmsModel = new ProcessDef();
//        pmsModel.setModelKey(bpmRequest.getModelKey());
//        pmsModel.setModelName(bpmRequest.getModelName());
//        pmsModel.setUserName(bpmRequest.getUser());
//        pmsModel.setId(bpmRequest.getId());
//        pmsModel.setModelXml(bpmRequest.getXmlStr());
        pmsModel.setStatus(bpmRequest.getStatus());
        if (pmsModel.getId() == 0) {
            pmsModel.setCreateTime(new Date());
            pmsModel.setUpdateTime(new Date());
            pmsModelRepository.insert(pmsModel);
        } else {
            pmsModel.setUpdateTime(new Date());
            pmsModelRepository.update(pmsModel);
        }
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







