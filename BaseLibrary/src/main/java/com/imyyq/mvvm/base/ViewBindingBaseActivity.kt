package com.imyyq.mvvm.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.imyyq.mvvm.widget.CustomLayoutDialog

/**
 * 通过构造函数和泛型，完成 view 的初始化和 vm 的初始化，并且将它们绑定，
 */
abstract class ViewBindingBaseActivity<V : ViewBinding, VM : BaseViewModel<out BaseModel>> :
    ParallaxSwipeBackActivity(),
    IView<V, VM>, ILoadingDialog, IActivityResult, IArgumentsFromIntent, View.OnClickListener {

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM

    private lateinit var mStartActivityForResult: ActivityResultLauncher<Intent>

    private val mLoadingDialog by lazy { CustomLayoutDialog(this, loadingDialogLayout()) }

    fun getLoadingDialog() = mLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isKeepScreenOn()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        mBinding = initBinding(layoutInflater, null)
        initBeforeSetContentView()
        initViewAndViewModel()
        initParam()
        initView()
        initListener()
        initUiChangeLiveData()
        initViewObservable()
        initData()
    }


    abstract override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): V

    @CallSuper
    override fun initViewAndViewModel() {
        setContentView(mBinding.root)
        mViewModel = initViewModel(this)
        mViewModel.mIntent = getArgumentsIntent()
        // 让 vm 可以感知 v 的生命周期
        lifecycle.addObserver(mViewModel)
    }

    override fun onDestroy() {
        super.onDestroy()

        // 界面销毁时移除 vm 的生命周期感知
        if (this::mViewModel.isInitialized) {
            lifecycle.removeObserver(mViewModel)
        }
        removeLiveDataBus(this)
    }

    /**
     * 通过 [BaseViewModel.startActivity] 传递 bundle，在这里可以获取
     */
    override fun getBundle(): Bundle? {
        return intent.extras
    }

    override fun getArgumentsIntent(): Intent? {
        return intent
    }

    final override fun initUiChangeLiveData() {
        if (isNeedLoadingDialog()) {
            mViewModel.mUiChangeLiveData.initLoadingDialogEvent()

            // 显示对话框
            mViewModel.mUiChangeLiveData.showLoadingDialogEvent?.observe(this, Observer {
                showLoadingDialog(mLoadingDialog, it)
            })
            // 隐藏对话框
            mViewModel.mUiChangeLiveData.dismissLoadingDialogEvent?.observe(this, Observer {
                dismissLoadingDialog(mLoadingDialog)
            })
        }
    }

    private fun initStartActivityForResult() {
        if (!this::mStartActivityForResult.isInitialized) {
            mStartActivityForResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    val data = it.data ?: Intent()
                    when (it.resultCode) {
                        Activity.RESULT_OK -> {
                            onActivityResultOk(data)
                            mViewModel.onActivityResultOk(data)
                        }
                        Activity.RESULT_CANCELED -> {
                            onActivityResultCanceled(data)
                            mViewModel.onActivityResultCanceled(data)
                        }
                        else -> {
                            onActivityResult(it.resultCode, data)
                            mViewModel.onActivityResult(it.resultCode, data)
                        }
                    }
                }
        }
    }

    /**
     * true 则当前界面常亮
     */
    protected open fun isKeepScreenOn() = false

    /**
     * <pre>
     *     // 一开始我们这么写
     *     mViewModel.liveData.observe(this, Observer { })
     *
     *     // 用这个方法可以这么写
     *     observe(mViewModel.liveData) { }
     *
     *     // 或者这么写
     *     observe(mViewModel.liveData, this::onChanged)
     *     private fun onChanged(s: String) { }
     * </pre>
     */
    fun <T> observe(liveData: LiveData<T>, onChanged: ((t: T) -> Unit)) =
        liveData.observe(this, Observer { onChanged(it) })

    /**
     * 如果加载中对话框可手动取消，并且开启了取消耗时任务的功能，则在取消对话框后调用取消耗时任务
     */
    @CallSuper
    override fun onCancelLoadingDialog() = mViewModel.cancelConsumingTask()
}