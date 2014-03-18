package foo

class FoobarTests extends GroovyTestCase {

    void testAppendSimple() {
        String given = "abc"
        String addtl = "def"
        String expected = "abcdef"

        assertEquals expected, given.append(addtl,1)
    }

}
