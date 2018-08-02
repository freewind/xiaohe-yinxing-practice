package github.freewind.xiaohe.practice

import github.freewind.xiaohe.chardecoder.XiaoHeCharDecoder
import github.freewind.xiaohe.chardecoder.allCharCodes
import github.freewind.xiaohe.practice.HelloWorldStyle.Companion.row
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.InputStream
import java.util.*

class HelloWorld : View() {

    private var charLabel by singleAssign<Label>()
    private var tipLabel by singleAssign<Label>()

    override val root = vbox {
        vbox {
            addClass(row)
            label("我") {
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
            }
        }
        imageview {
            image = Image(resourceStream("hexing.png")!!)
        }
    }

    private fun checkInput(char: Char, code: String) {
        val found = XiaoHeCharDecoder.findCode(char)!!
        if (found.code.equals(code, true)) {
            charLabel.text = nextChar().toString()
            tipLabel.text = ""
        } else {
            tipLabel.text = found.code
        }
    }

    private fun nextChar(): Char {
        return allCharCodes.get(Random().nextInt(allCharCodes.size)).char
    }

    private fun resourceStream(path: String): InputStream? {
        return this.javaClass.classLoader.getResourceAsStream(path)
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