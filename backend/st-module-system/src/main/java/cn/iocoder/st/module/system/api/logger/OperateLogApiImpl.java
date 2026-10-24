package cn.iocoder.st.module.system.api.logger;

import cn.iocoder.st.framework.common.pojo.PageResult;
import cn.iocoder.st.framework.common.util.object.BeanUtils;
import cn.iocoder.st.framework.common.biz.system.logger.dto.OperateLogCreateReqDTO;
import cn.iocoder.st.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.iocoder.st.module.system.api.logger.dto.OperateLogRespDTO;
import cn.iocoder.st.module.system.dal.dataobject.logger.OperateLogDO;
import cn.iocoder.st.module.system.service.logger.OperateLogService;
import org.dromara.core.trans.anno.TransMethodResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 操作日志 API 实现类
 */
@Service
@Validated
public class OperateLogApiImpl implements OperateLogApi {

    @Resource
    private OperateLogService operateLogService;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        operateLogService.createOperateLog(createReqDTO);
    }

    @Override
    @TransMethodResult
    public PageResult<OperateLogRespDTO> getOperateLogPage(OperateLogPageReqDTO pageReqDTO) {
        PageResult<OperateLogDO> operateLogPage = operateLogService.getOperateLogPage(pageReqDTO);
        return BeanUtils.toBean(operateLogPage, OperateLogRespDTO.class);
    }

}
