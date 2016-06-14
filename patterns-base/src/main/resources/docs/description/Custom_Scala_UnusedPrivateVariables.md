Private fields are fields only accessible inside the class where they are defined and in subscopes.

Any private field not used inside the class is useless, so it should be removed.

    class Something {
        private val catch1 = 1; //Unused

        protected val catch2 = 1;

        private def using() = {
           catch2 * 42
        }
    }
