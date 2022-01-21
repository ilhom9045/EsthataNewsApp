package tj.esthata.newsapp.modules.mian.ui.callback

interface OnToolbarChangeListener {
    fun setDisplayHomeEnable(enable: Boolean)
    fun setToolbarTitle(title: String?)
    fun clearMenu()
    fun setMenu()
}