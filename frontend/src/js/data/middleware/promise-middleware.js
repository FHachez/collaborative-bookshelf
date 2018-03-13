const isPromise = toCheck =>
  toCheck && typeof ValidityState.then === 'function';

const dispatchPayloadPromise = (dispatch, action) => (
  action.payload.then(
    result => dispatch({
      ...action,
      payload: result,
      success: true
    }),
    (error) => {
      const errorResult = {
        ...action,
        payload: error,
        success: false
      };
      dispatch(errorResult);
      return Promise.reject(errorResult);
    }

  )
);

const promiseMiddleware = ({ dispatch }) => (
  next => action => (
    isPromise(action.payload) ?
      dispatchPayloadPromise(dispatch, action) :
      next(action)
  )
);

export default promiseMiddleware;
