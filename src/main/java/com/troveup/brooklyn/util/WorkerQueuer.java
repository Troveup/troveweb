package com.troveup.brooklyn.util;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.Map;

/**
 * Created by tim on 5/26/15.
 */
public class WorkerQueuer
{
    public static void queueWorkForWorker(Map<String, String> messageParams, String url)
    {
        Queue queue = QueueFactory.getDefaultQueue();

        TaskOptions options = TaskOptions.Builder.withUrl(url);

        for (String key : messageParams.keySet())
        {
            options.param(key, messageParams.get(key));
        }

        queue.add(options);
    }

    public static void queueWorkForWorker(Map<String, String> messageParams, String url, String queue)
    {
        Queue workerQueue = QueueFactory.getQueue(queue);

        TaskOptions options = TaskOptions.Builder.withUrl(url);

        for (String key : messageParams.keySet())
        {
            options.param(key, messageParams.get(key));
        }

        workerQueue.add(options);
    }
}
