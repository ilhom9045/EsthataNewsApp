package tj.esthata.newsapp.modules.main.ui.callback

interface OnToolbarChangeListener {
    fun setDisplayHomeEnable(enable: Boolean)
    fun setToolbarTitle(title: String?)
    fun clearMenu()
    fun setMenu()
}