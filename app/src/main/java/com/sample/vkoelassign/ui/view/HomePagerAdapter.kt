import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sample.vkoelassign.R
import com.sample.vkoelassign.ui.view.MyFeedFragment
import com.sample.vkoelassign.ui.view.MyPostFragment

class HomePagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    //Returns item at a particular position
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = MyFeedFragment()
                /* val bundle = Bundle()
                 bundle.putBoolean("show_disconnect", false)
                 fragment.arguments = bundle*/
            }
            1 -> {
                //My Cards
                fragment = MyPostFragment()
                /*  val bundle = Bundle()
                  bundle.putBoolean("show_disconnect", true)
                  fragment.arguments = bundle*/
            }
        }
        return fragment!!
    }

    /**
     * Returns no of Screens initialized
     */
    override fun getCount(): Int {
        return 2
    }

    /**
     * Method to set Title to Tabs
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            context.getString(R.string.txt_my_feed)
        } else {
            context.getString(R.string.txt_my_post)
        }
    }
}