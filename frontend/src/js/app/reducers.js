import { combineReducers } from 'redux';

import loginReducer from '../scenes/login/data/login-reducer';
import medicinesReducer from '../scenes/medicines/data/medicines-reducer';
import navBarReducer from '../scenes/navbar/data/navbar-reducer';

const rootReducer = combineReducers({
  pages: combineReducers({
    login: combineReducers({
      store: loginReducer,
    }),
    medicines: combineReducers({
      store: medicinesReducer,
    }),
    navBar: combineReducers({
      store: navBarReducer,
    }),
  }),
});

export default rootReducer;
