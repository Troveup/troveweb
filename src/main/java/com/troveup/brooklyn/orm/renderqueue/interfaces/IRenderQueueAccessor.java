package com.troveup.brooklyn.orm.renderqueue.interfaces;

import com.troveup.brooklyn.orm.common.interfaces.IEnums;
import com.troveup.brooklyn.orm.renderqueue.model.Render;

import java.util.List;
import java.util.Map;

/**
 * Created by tim on 6/23/15.
 */
public interface IRenderQueueAccessor
{
    enum RENDER_STATUS
    {
        QUEUED,
        SUBMITTED,
        RUNNING,
        COMPLETE,
        ERROR
    }

    Boolean submitRender(Render render);
    Boolean submitRenders(List<Render> render);
    Render getRender(Long renderId, IEnums.SEEK_MODE mode);
    Render getRender(String jobId, IEnums.SEEK_MODE mode);
    List<Render> getRenderByItem(Long itemId, IEnums.SEEK_MODE mode);
    Boolean updateRenderStatus(String jobId, RENDER_STATUS status, String errorMessage, int errorId);
    List<Render> getQueuedRenders();
    Render getRunningRender();
    List<Render> getFtueBasedRenders(List<Long> ftuePersistedRecordIdentifiers, IEnums.SEEK_MODE mode);
    Map<Long, Boolean> checkFtueRenderStatuses(List<Long> ftueIdentifiers);
    Boolean incrementRenderRetryCount(String renderJobId);

    List<Render> getRendersByStatus(RENDER_STATUS status);

}
