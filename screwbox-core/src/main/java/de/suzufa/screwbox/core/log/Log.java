package de.suzufa.screwbox.core.log;

public interface Log {

    Log debug(String message);

    Log info(String message);

    Log warning(String message);

    Log error(String message);

    Log error(String message, Exception e);

    Log setAdapter(LoggingAdapter adapter);
}
