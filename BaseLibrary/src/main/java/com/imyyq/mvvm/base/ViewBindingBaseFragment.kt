package com.imyyq.mvvm.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.imyyq.mvvm.widget.CustomLayoutDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class ViewBindingBaseFragment<V : ViewBinding, VM : BaseViewModel<out BaseModel>> :
    Fragment(),
    IView<V, VM>, ILoadingDialog, IActivityResult {

    protected lateinit var mBinding: V
    protected lateinit var mViewModel: VM

    private lateinit var mStartActivityForResult: ActivityResultLauncher<Intent>

    private val mLoadingDialog by lazy { CustomLayoutDialog(requireActivity(), loadingDialogLayout()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = initBinding(inflater, container)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBeforeSetContentView()
        initViewAndViewModel()
        initParam()
        initView()
        initListener()
        initUiChangeLiveData()
        initViewObservable()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 通过反射，解决内存泄露问题
        GlobalScope.launch {
            var clz: Class<*>? = this@ViewBindingBaseFragment.javaClass
            while (clz != null) {
                // 找到 mBinding 所在的类
                if (clz == ViewBindingBaseFragment::class.java) {
                    try {
                        val field = clz.getDeclaredField("mBinding")
                        field.isAccessible = true
                        field.set(this@ViewBindingBaseFragment, null)
                    } catch (ignore: Exception) {
                    }
                }
                clz = clz.superclass
            }
        }
    }

    @CallSuper
    override fun initViewAndViewModel() {
        mViewModel = initViewModel(this)
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
     * 通过 setArguments 传递 bundle，在这里可以获取
     */
    override fun getBundle(): Bundle? {
        return arguments
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