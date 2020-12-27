package goldenhuaji.me.fanchess.ui

import java.awt.*
import java.util.ArrayList
import javax.swing.*
import javax.swing.border.Border

/**
 * MyVFlowLayout is similar to FlowLayout except it lays out components
 * vertically. Extends FlowLayout because it mimics much of the behavior of the
 * FlowLayout class, except vertically. An additional feature is that you can
 * specify a fill to edge flag, which causes the MyVFlowLayout manager to
 * resize all components to expand to the column width Warning: This causes
 * problems when the main panel has less space that it needs and it seems to
 * prohibit multi-column output. Additionally there is a vertical fill flag,
 * which fills the last component to the remaining height of the container.
 */
class MyVFlowLayout @JvmOverloads constructor(
    verticalAlignment: Int = TOP,
    horizontalAlignment: Int = MIDDLE,
    horizontalGap: Int = 5,
    verticalGap: Int = 5,
    topVerticalGap: Int = 5,
    bottomVerticalGap: Int = 5,
    isHorizontalFill: Boolean = true,
    isVerticalFill: Boolean = false
) :
    FlowLayout() {
    private var horizontalAlignment = 0
    var topVerticalGap = 0
    var bottomVerticalGap = 0
    /**
     * Returns true if the layout horizontally fills.
     *
     * @return true if horizontally fills.
     */
    /**
     * Set to true to enable horizontally fill.
     *
     * @param hfill
     * true to fill horizontally.
     */
    var horizontalFill = false
    /**
     * Returns true if the layout vertically fills.
     *
     * @return true if vertically fills the layout using the specified.
     */
    /**
     * Set true to fill vertically.
     *
     * @param isVerticalFill
     * true to fill vertically.
     */
    var verticalFill = false

    /**
     * Construct a new MyVFlowLayout.
     *
     * @param verticalAlignment the alignment value
     * @param horizontalAlignment the horizontal alignment value
     * @param horizontalGap  the horizontal gap variable
     * @param verticalGap  the vertical gap variable
     * @param topVerticalGap the top vertical gap variable
     * @param bottomVerticalGap the bottom vertical gap variable
     * @param isHorizontalFill  the fill to edge flag
     * @param isVerticalFill true if the panel should vertically fill.
     */
    init {
        this.alignment = verticalAlignment
        setHorizontalAlignment(horizontalAlignment)
        hgap = horizontalGap
        vgap = verticalGap
        this.topVerticalGap = topVerticalGap
        this.bottomVerticalGap = bottomVerticalGap
        horizontalFill = isHorizontalFill
        verticalFill = isVerticalFill
    }

    constructor(isHorizontalFill: Boolean, isVerticalFill: Boolean) : this(
        TOP,
        MIDDLE,
        5,
        5,
        5,
        5,
        isHorizontalFill,
        isVerticalFill
    ) {
    }

//    constructor(align: Int) : this(align, MIDDLE, 5, 5, 5, 5, true, false) {}
    constructor(align: Int, isHorizontalFill: Boolean, isVerticalFill: Boolean) : this(
        align,
        MIDDLE,
        5,
        5,
        5,
        5,
        isHorizontalFill,
        isVerticalFill
    ) {
    }

    constructor(
        align: Int,
        horizontalGap: Int,
        verticalGap: Int,
        isHorizontalFill: Boolean,
        isVerticalFill: Boolean
    ) : this(align, MIDDLE, horizontalGap, verticalGap, verticalGap, verticalGap, isHorizontalFill, isVerticalFill) {
    }

    constructor(
        align: Int,
        horizontalGap: Int,
        verticalGap: Int,
        topVerticalGap: Int,
        bottomVerticalGap: Int,
        isHorizontalFill: Boolean,
        isVerticalFill: Boolean
    ) : this(
        align,
        MIDDLE,
        horizontalGap,
        verticalGap,
        topVerticalGap,
        bottomVerticalGap,
        isHorizontalFill,
        isVerticalFill
    ) {
    }

    fun setHorizontalAlignment(horizontalAlignment: Int) {
        if (LEFT == horizontalAlignment) {
            this.horizontalAlignment = LEFT
        } else if (RIGHT == horizontalAlignment) {
            this.horizontalAlignment = RIGHT
        } else {
            this.horizontalAlignment = MIDDLE
        }
    }

    fun getHorizontalAlignment(): Int {
        return horizontalAlignment
    }

    override fun setHgap(horizontalGap: Int) {
        super.setHgap(horizontalGap)
    }

    override fun setVgap(verticalGap: Int) {
        super.setVgap(verticalGap)
    }

    /**
     * Returns the preferred dimensions given the components in the target
     * container.
     *
     * @param container
     * the component to lay out
     */
    override fun preferredLayoutSize(container: Container): Dimension {
        val rs = Dimension(0, 0)
        val components = getVisibleComponents(container)
        val dimension = preferredComponentsSize(components)
        rs.width += dimension.width
        rs.height += dimension.height
        val insets = container.insets
        rs.width += insets.left + insets.right
        rs.height += insets.top + insets.bottom
        if (0 < components.size) {
            rs.width += hgap * 2
            rs.height += topVerticalGap
            rs.height += bottomVerticalGap
        }
        return rs
    }

    /**
     * Returns the minimum size needed to layout the target container.
     *
     * @param container
     * the component to lay out.
     * @return the minimum layout dimension.
     */
    override fun minimumLayoutSize(container: Container): Dimension {
        val rs = Dimension(0, 0)
        val components = getVisibleComponents(container)
        val dimension = minimumComponentsSize(components)
        rs.width += dimension.width
        rs.height += dimension.height
        val insets = container.insets
        rs.width += insets.left + insets.right
        rs.height += insets.top + insets.bottom
        if (0 < components.size) {
            rs.width += hgap * 2
            rs.height += topVerticalGap
            rs.height += bottomVerticalGap
        }
        return rs
    }

    override fun layoutContainer(container: Container) {
        val horizontalGap = hgap
        val verticalGap = vgap
        val insets = container.insets
        val maxWidth = container.size.width - (insets.left + insets.right + horizontalGap * 2)
        val maxHeight = container.size.height - (insets.top + insets.bottom + topVerticalGap + bottomVerticalGap)
        val components = getVisibleComponents(container)
        val preferredComponentsSize = preferredComponentsSize(components)
        val alignment = this.alignment
        var y = insets.top + topVerticalGap
        if (!verticalFill && preferredComponentsSize.height < maxHeight) {
            if (MIDDLE == alignment) {
                y += (maxHeight - preferredComponentsSize.height) / 2
            } else if (BOTTOM == alignment) {
                y += maxHeight - preferredComponentsSize.height
            }
        }
        var index = 0
        for (component in components) {
            var x = insets.left + horizontalGap
            val dimension = component.preferredSize
            if (horizontalFill) {
                dimension.width = maxWidth
            } else {
                dimension.width = Math.min(maxWidth, dimension.width)
                if (MIDDLE == horizontalAlignment) {
                    x += (maxWidth - dimension.width) / 2
                } else if (RIGHT == horizontalAlignment) {
                    x += maxWidth - dimension.width
                }
            }
            if (verticalFill && index == components.size - 1) {
                val height = maxHeight + topVerticalGap + insets.top - y
                dimension.height = Math.max(height, dimension.height)
            }
            component.size = dimension
            component.setLocation(x, y)
            y += dimension.height + verticalGap
            index++
        }
    }

    private fun preferredComponentsSize(components: List<Component>): Dimension {
        val rs = Dimension(0, 0)
        for (component in components) {
            val dimension = component.preferredSize
            rs.width = Math.max(rs.width, dimension.width)
            rs.height += dimension.height
        }
        if (0 < components.size) {
            rs.height += vgap * (components.size - 1)
        }
        return rs
    }

    private fun minimumComponentsSize(components: List<Component>): Dimension {
        val rs = Dimension(0, 0)
        for (component in components) {
            val dimension = component.minimumSize
            rs.width = Math.max(rs.width, dimension.width)
            rs.height += dimension.height
        }
        if (0 < components.size) {
            rs.height += vgap * (components.size - 1)
        }
        return rs
    }

    private fun getVisibleComponents(container: Container): List<Component> {
        val rs: MutableList<Component> = ArrayList()
        for (component in container.components) {
            if (component.isVisible) {
                rs.add(component)
            }
        }
        return rs
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L

        /**
         * Specify alignment top.
         */
        const val TOP = 0

        /**
         * Specify a middle alignment.
         */
        const val MIDDLE = 1

        /**
         * Specify the alignment to be bottom.
         */
        const val BOTTOM = 2

        /**
         * Specify the alignment to be left.
         */
        const val LEFT = 0

        /**
         * Specify the alignment to be right.
         */
        const val RIGHT = 2

        @JvmStatic
        fun main(args: Array<String>) {
            println("Just for test ...")
            val frame = JFrame()
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.setBounds(0, 0, 300, 500)
            val contentPanel = JPanel()
            val padding = BorderFactory.createEmptyBorder(5, 5, 5, 5)
            contentPanel.border = padding
            contentPanel.background = Color.LIGHT_GRAY
            val myVFlowLayout = MyVFlowLayout(1, 0, 5, 5, 20, 40, true, false)
            contentPanel.layout = myVFlowLayout
            val scrollPane = JScrollPane(contentPanel)
            frame.add(scrollPane)
            for (i in 0..6) {
                val button = JButton((i + 1).toString())
                button.preferredSize = Dimension(50, 30)
                contentPanel.add(button)
            }
            val specButton = JButton("spec")
            specButton.preferredSize = Dimension(100, 50)
            contentPanel.add(specButton)
            specButton.isVisible = true
            for (i in 0..19) {
                val button = JButton((i + 1).toString())
                button.preferredSize = Dimension(90, 30)
                contentPanel.add(button)
            }
            frame.isVisible = true
        }
    }
}