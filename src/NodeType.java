enum NodeType {

    /*
     * FOR EASE OF ORGANIZATION, NODES ALREADY HANDLED
     * BELOW WILL BE DENOTED BY AN 'X'.
     */

    //STATEMENTS
    PROGRAM, //                 -X
    VARIABLEDECLARATION, //     -X
    FUNCDECLARATION, //         -X
    IFSTATEMENT, //             -X
    FORSTATEMENT, //            -X
    WHILESTATEMENT, //          -X
    TRYCATCHSTATEMENT, //       -X

    //EXPRESSIONS
    ASSIGNMENTEXPR, //          -X
    BINARYEXPR, //              -X
    MEMBEREXPR, //              -X
    CALLEXPR, //                -X

    //LITERALS
    IDENTIFIER, //              -X
    INTLITERAL, //              -X
    FLOATLITERAL, //            -X
    OBJLITERAL, //              -X
    ARRAYLITERAL, //            -X
    STRINGLITERAL, //           -X
    NULLLITERAL, //             -X
    BOOLLITERAL, //             -X

}