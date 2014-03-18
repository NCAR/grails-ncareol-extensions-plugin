package foo2

class FoobarTests extends GroovyTestCase {

    void testAppendSimple() {
        String given = "given"
        String addtl = "addtl"
        String expected = "givenaddtl"

        println "expected = ${expected}"

        assertEquals expected, given.append(addtl,1)
    }

}
