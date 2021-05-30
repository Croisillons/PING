package fr.epita.assistants.myide.domain.entity;

import fr.epita.assistants.utils.Given;

import javax.validation.constraints.NotNull;

@Given(overridden = true)
public interface Feature {

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @NotNull ExecutionReport execute(final Project project, final Object... params);

    /**
     * @return The type of the Feature.
     */
    @NotNull Type type();

    interface ExecutionReport {
        boolean isSuccess();
    }

    interface Type {}
}
