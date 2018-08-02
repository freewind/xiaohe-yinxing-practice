package github.freewind.xiaohe.practice

import github.freewind.xiaohe.practice.HelloWorldStyle.Companion.row
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.InputStream

class HelloWorld : View() {

    private var charLabel by singleAssign<Label>()

    override val root = vbox {
        hbox {
            addClass(row)
            label("我") {
                charLabel = this
                addClass(HelloWorldStyle.charLabel)
            }
            VBox.setVgrow(this, Priority.ALWAYS)
        }
        hbox {
            addClass(row)
            textfield {
                addClass(HelloWorldStyle.input)
            }
        }
        imageview {
            image = Image(resourceStream("hexing.png")!!)
        }
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