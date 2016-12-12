package hesso.mas.stdhb.DataAccess.Communication.AsyncTask;

/**
 * Created by chf on 28.08.2016.
 *
 * AsyncTask enables proper and easy use of the UI thread. This class allows to perform background operations
 * and publish results on the UI thread without having to manipulate threads and/or handlers.
 *
 * AsyncTask is designed to be a helper class around Thread and Handler and does not constitute a generic
 * threading framework. AsyncTasks should ideally be used for short operations (a few seconds at the most.)
 * If you need to keep threads running for long periods of time, it is highly recommended you use the various
 * APIs provided by the java.util.concurrent package such as Executor, ThreadPoolExecutor and FutureTask.
 *
 * An asynchronous task is defined by a computation that runs on a background thread and whose result is published
 * on the UI thread. An asynchronous task is defined by 3 generic types, called Params, Progress and Result,
 * and 4 steps, called onPreExecute, doInBackground, onProgressUpdate and onPostExecute.
 */
public interface IRetrieveCitizenDataAsyncTask {

    /**
     * Perform a computation on a background thread.
     * The specified parameters are the parameters passed to execute(Params...) by the caller
     * of this task.
     *
     * @param urls
     * @return
     */
    String doInBackground(String... urls);

}
