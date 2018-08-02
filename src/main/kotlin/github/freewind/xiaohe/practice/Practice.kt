package github.freewind.xiaohe.practice

import github.freewind.xiaohe.chardecoder.XiaoHeCharDecoder
import github.freewind.xiaohe.practice.HelloWorldStyle.Companion.row
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.InputStream
import java.util.*
import com.sun.javaws.BrowserSupport.showDocument
import javafx.application.Application


class HelloWorld : View("小鹤音形码练习") {

    private var charLabel by singleAssign<Label>()
    private var tipLabel by singleAssign<Label>()

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
        }
        vbox {
            addClass(row)
            label {
                charLabel = this
                addClass(HelloWorldStyle.charLabel)
            }
            label { tipLabel = this }
            VBox.setVgrow(this, Priority.ALWAYS)
        }
        hbox {
            addClass(row)
            textfield {
                addClass(HelloWorldStyle.input)
                setOnKeyReleased { event ->
                    val code = this.text
                    if (code.length == 4 || event.code == KeyCode.SPACE) {
                        checkInput(charLabel.text.single(), code)
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
            hyperlink("小鹤官网").setOnAction { openInBrowser("http://www.flypy.com/") }
            hyperlink("鹤形规则详解").setOnAction { openInBrowser("http://www.flypy.com/xing.html") }
            hyperlink("小鹤查形").setOnAction { openInBrowser("http://www.xhup.club") }
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
            val partInfo = found.parts.map { it.name + (it.code?.let { "($it)" } ?: "") }
            tipLabel.text = found.longestCode + ": " + partInfo

        }
    }

    private fun setNewChar() {
        charLabel.text = nextChar().toString()
        tipLabel.text = ""
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

class HelloWorldStyle : Stylesheet() {
    companion object {
        val row by cssclass()
        val input by cssclass()
        val charLabel by cssclass()
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
    }

}

class HelloWorldApp : App(HelloWorld::class, HelloWorldStyle::class)

fun main(args: Array<String>) {
    launch<HelloWorldApp>()
}