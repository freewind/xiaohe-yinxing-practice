package github.freewind.xiaohe.practice

import github.freewind.xiaohe.chardecoder.XiaoHeCharDecoder
import github.freewind.xiaohe.practice.XiaoheStyle.Companion.row
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.InputStream
import java.net.URLEncoder
import java.util.*

class XiaoHeView : View("小鹤音形码练习") {

    private var charLabel by singleAssign<Label>()
    private var inputField by singleAssign<TextField>()
    private var tipContainer by singleAssign<Node>()

    private object CharLevelSelected {
        var level1 by singleAssign<CheckBox>()
        var level2 by singleAssign<CheckBox>()
        var level3 by singleAssign<CheckBox>()
    }

    override val root = vbox {
        hbox {
            checkbox("一级字") {
                CharLevelSelected.level1 = this
                this.isSelected = true
            }
            checkbox("二级字") { CharLevelSelected.level2 = this }
            checkbox("三级字") { CharLevelSelected.level3 = this }
            spacing = 10.0
        }
        vbox {
            addClass(row)
            label {
                charLabel = this
                addClass(XiaoheStyle.charLabel)
            }
            VBox.setVgrow(this, Priority.ALWAYS)
        }
        hbox {
            addClass(XiaoheStyle.row, XiaoheStyle.tipContainer)
            tipContainer = this
        }
        hbox {
            addClass(row)
            textfield {
                inputField = this
                addClass(XiaoheStyle.input)
                setOnKeyReleased { event ->
                    val code = this.text
                    if (code.length == 4 || event.code == KeyCode.SPACE) {
                        showTips()
                        this.text = ""
                    }
                }
                runLater { this.requestFocus() }
            }
        }
        imageview {
            image = Image(resourceStream("hexing.png")!!)
        }
        hbox {
            addClass(row)
            add(link("http://www.flypy.com/", "小鹤官网"))
            add(link("http://www.flypy.com/xing.html", "鹤形规则详解"))
            add(link("http://www.xhup.club", "小鹤查形"))
            hyperlink {
                charLabel.textProperty().addListener { _, _, char ->
                    this.text = "汉典查看 '$char'"
                    this.setOnAction {
                        openInBrowser(zdicUrl(char))
                    }
                }
            }
            button("?").setOnAction { _ ->
                title = "提示编码"
                showTips()
            }
        }
    }

    private fun showTips() {
        checkInput(charLabel.text.single(), inputField.text)
    }

    private fun zdicUrl(char: String?) = "http://www.zdic.net/sousuo?q=${URLEncoder.encode(char, "UTF-8")}"

    private fun link(url: String, text: String): Hyperlink {
        return hyperlink(text) {
            setOnAction { openInBrowser(url) }
        }
    }

    init {
        setNewChar()
    }

    private fun checkInput(char: Char, code: String) {
        val found = XiaoHeCharDecoder.findCode(char)!!
        if (found.longestCode.equals(code, true)) {
            setNewChar()
        } else {
            val tips = hbox {
                label(found.longestCode)
                found.parts.forEach {
                    add(link(zdicUrl(it.name), it.name))
                    if (it.code != null) {
                        label("(${it.code}) ")
                    }
                }
            }
            tipContainer.replaceChildren(tips)
        }
    }

    private fun setNewChar() {
        charLabel.text = nextChar().toString()
        tipContainer.replaceChildren()
    }

    private fun nextChar(): Char {
        val chars = listOf(
                XiaoHeCharDecoder.charsLevel1 to CharLevelSelected.level1.isSelected,
                XiaoHeCharDecoder.charsLevel2 to CharLevelSelected.level2.isSelected,
                XiaoHeCharDecoder.charsLevel3 to CharLevelSelected.level3.isSelected
        ).filter { it.second }.map { it.first }.flatten()
        return chars.get(Random().nextInt(chars.size)).char
    }

    private fun resourceStream(path: String): InputStream? {
        return this.javaClass.classLoader.getResourceAsStream(path)
    }

    private fun openInBrowser(url: String) {
        hostServices.showDocument(url)
    }

}

class XiaoheStyle : Stylesheet() {
    companion object {
        val row by cssclass()
        val input by cssclass()
        val charLabel by cssclass()
        val tipContainer by cssclass()
    }

    init {
        root {
            prefWidth = 600.px
            prefHeight = 800.px
            alignment = Pos.CENTER
            spacing = 20.px
            padding = box(20.px)
        }
        charLabel {
            fontSize = 300.px
        }
        row {
            alignment = Pos.CENTER
        }
        input {
            prefWidth = 200.px
            fontSize = 40.px
        }
        tipContainer {
            //            prefHeight = 30.px
        }
    }

}

class XiaoHeGui : App(XiaoHeView::class, XiaoheStyle::class)

fun main(args: Array<String>) {
    launch<XiaoHeGui>()
}

object XiaoHePractice {
    @JvmStatic
    fun main(args: Array<String>) {
        launch<XiaoHeGui>()
    }
}