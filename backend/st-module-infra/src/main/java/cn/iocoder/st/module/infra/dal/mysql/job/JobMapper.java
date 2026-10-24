package cn.iocoder.st.module.infra.dal.mysql.job;

import cn.iocoder.st.framework.common.pojo.PageResult;
import cn.iocoder.st.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.st.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.st.module.infra.controller.admin.job.vo.job.JobPageReqVO;
import cn.iocoder.st.module.infra.dal.dataobject.job.JobDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务 Mapper
 */
@Mapper
public interface JobMapper extends BaseMapperX<JobDO> {

    default JobDO selectByHandlerName(String handlerName) {
        return selectOne(JobDO::getHandlerName, handlerName);
    }

    default PageResult<JobDO> selectPage(JobPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<JobDO>()
                .likeIfPresent(JobDO::getName, reqVO.getName())
                .eqIfPresent(JobDO::getStatus, reqVO.getStatus())
                .likeIfPresent(JobDO::getHandlerName, reqVO.getHandlerName())
                .orderByDesc(JobDO::getId));
    }

}
