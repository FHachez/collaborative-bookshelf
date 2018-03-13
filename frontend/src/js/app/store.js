import { createStore, applyMiddleware } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import { apiMiddleware } from 'redux-api-middleware';

import authMiddleware from '../data/middleware/auth-middleware';
import promiseMiddleware from '../data/middleware/promise-middleware';
import logoutMiddleware from '../data/middleware/logout-middleware';

import rootReducer from './reducers';
import preload from '../services/preload';

const middleware = composeWithDevTools(
  applyMiddleware(
    authMiddleware,
    apiMiddleware,
    promiseMiddleware,
    logoutMiddleware
  ),
);

const preloadedState = preload();

const initalStore = createStore(rootReducer, preloadedState, middleware);

export default initalStore;
